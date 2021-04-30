package com.microsoft.thrifty.transport

import io.ktor.utils.io.core.*

class PacketTransport(private val packetBuilder: BytePacketBuilder): Transport() {
    override fun read(buffer: ByteArray, offset: Int, count: Int): Int {
        return packetBuilder.preview { packet ->
            packet.readAvailable(buffer, offset, count)
        }
    }

    override fun write(buffer: ByteArray, offset: Int, count: Int) {
        packetBuilder.writeFully(buffer, offset, count)
    }

    override fun flush() {
        packetBuilder.flush()
    }

    override fun close() {
        packetBuilder.close()
    }
}

class PacketInputTransport(private val packet: ByteReadPacket): Transport() {
    override fun read(buffer: ByteArray, offset: Int, count: Int): Int {
        return packet.readAvailable(buffer, offset, count)
    }

    override fun write(buffer: ByteArray, offset: Int, count: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun flush() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun close() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
