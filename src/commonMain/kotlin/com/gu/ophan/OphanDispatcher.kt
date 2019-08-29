package com.gu.ophan

import com.microsoft.thrifty.Struct
import com.microsoft.thrifty.protocol.CompactProtocol
import com.soywiz.klock.DateTime
import com.theguardian.tracksuite.com.microsoft.thrifty.transport.PacketInputTransport
import com.theguardian.tracksuite.com.microsoft.thrifty.transport.PacketTransport
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.response.HttpResponse
import io.ktor.client.response.readText
import io.ktor.http.ContentType
import io.ktor.http.content.ByteArrayContent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.io.core.ByteReadPacket
import kotlinx.io.core.buildPacket
import kotlinx.io.core.readBytes
import kotlinx.io.core.use
import ophan.thrift.nativeapp.App
import ophan.thrift.nativeapp.Device
import ophan.thrift.nativeapp.Event
import ophan.thrift.nativeapp.NativeAppSubmission
import kotlin.coroutines.CoroutineContext

@Suppress("MemberVisibilityCanBePrivate", "unused")
class OphanDispatcher(
    private val app: App,
    private val device: Device,
    private val deviceId: String,
    private val userId: String,
    private val logger: Logger?,
    private val recordStore: RecordStore,
    private val coroutineContext: CoroutineContext
) {
    constructor(app: App, device: Device, deviceId: String, userId: String, logger: Logger?, recordStore: RecordStore) :
            this(app, device, deviceId, userId, logger, recordStore, getDefaultCoroutineContext())

    constructor(app: App, device: Device, deviceId: String, userId: String, logger: Logger?) :
            this(app, device, deviceId, userId, logger, InMemoryRecordStore())

    init {
        logger?.debug("OphanDispatcher", "Ophan dispatcher created")
    }

    private val ophanUrl = "https://ophan.theguardian.com/mob-loopback"
    private val thriftContentType = ContentType("application", "vnd.apache.thrift.compact")

    fun dispatchEvent(event: Event) {
        logger?.debug("OphanDispatcher", "About to pass coroutineContext...")
        dispatchEvent(event, coroutineContext)
    }

    fun dispatchEvent(event: Event, context: CoroutineContext) {
        logger?.debug("OphanDispatcher", "About to invoke function with coroutineContext...")
        CoroutineScope(context).launch {
            logger?.debug("OphanDispatcher", "Event B")
            storeAndSendEvent(event)
        }
    }

    private suspend fun storeAndSendEvent(event: Event) {
        val now = DateTime.now()
        val record = buildPacket {
            writeLong(now.unixMillisLong)
            val transport = PacketTransport(this)
            val protocol = CompactProtocol(transport)
            event.write(protocol)
        }.readBytes()
        val key = event.eventId
        recordStore.putRecord(key, record)
        logger?.debug("OphanDispatcher", "putting record $key")
        sendEvents()
    }

    private suspend fun sendEvents() {
        val records = recordStore.getRecords()
        val nowMillis = DateTime.now().unixMillisLong
        val events = records
                .map { record -> ByteReadPacket(record) }
                .mapNotNull { packet ->
                    try {
                        val millisWhenStored = packet.readLong()
                        val transport = PacketInputTransport(packet)
                        val protocol = CompactProtocol(transport)
                        val e = Event.ADAPTER.read(protocol)
                        val ageMillis = nowMillis - millisWhenStored
                        logger?.debug("OphanDispatcher", "ageMsLong=$ageMillis")
                        e.copy(ageMsLong = ageMillis)
                    } catch (e: Throwable) {
                        logger?.warn("OphanDispatcher", "unable to decode record", e)
                        null
                    }
                }
        if (events.isNotEmpty()) {
            logger?.debug("OphanDispatcher", "sending ${events.size} events")
            val submission = NativeAppSubmission.Builder()
                    .app(app)
                    .device(device)
                    .deviceId(deviceId)
                    .userId(userId)
                    .events(events)
                    .build()
            try {
                sendSubmission(submission)
                events.forEach { recordStore.removeRecord(it.eventId) }
            } catch (e: Exception) {
                logger?.warn("OphanDispatcher", "failed to send ${events.size} events", e)
            }
        } else {
            logger?.warn("OphanDispatcher", "no events to send!")
        }
    }

    private suspend fun sendSubmission(submission: NativeAppSubmission): HttpResponse {
        val response = HttpClient().use { client ->
            client.post<HttpResponse>(ophanUrl) {
                body = ByteArrayContent(toBytes(submission), thriftContentType)
            }
        }
        logger?.debug("OphanDispatcher", response.readText())
        logger?.debug("OphanDispatcher","It worked, the current version is 0.1.0")
        return response
    }

    private fun toBytes(struct: Struct): ByteArray {
        val packet = buildPacket {
            val transport = PacketTransport(this)
            val protocol = CompactProtocol(transport)
            struct.write(protocol)
        }
        return ByteArray(packet.remaining.toInt()).apply {
            packet.readAvailable(this)
        }
    }
}
