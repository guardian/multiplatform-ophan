package ophan.thrift.nativeapp

import com.microsoft.thrifty.Adapter
import com.microsoft.thrifty.Struct
import com.microsoft.thrifty.StructBuilder
import com.microsoft.thrifty.TType
import com.microsoft.thrifty.ThriftException
import com.microsoft.thrifty.ThriftField
import com.microsoft.thrifty.protocol.Protocol
import com.microsoft.thrifty.util.ProtocolUtil
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.collections.ArrayList
import kotlin.collections.List
import kotlin.jvm.JvmField
import kotlin.jvm.JvmStatic
import ophan.thrift.benchmark.BenchmarkData
import ophan.thrift.benchmark.NetworkOperationData
import ophan.thrift.componentEvent.ComponentEvent
import ophan.thrift.event.AbTestInfo
import ophan.thrift.event.Acquisition
import ophan.thrift.event.Interaction
import ophan.thrift.event.MediaPlayback
import ophan.thrift.event.Referrer
import ophan.thrift.event.RenderedAd
import ophan.thrift.event.Url
import ophan.thrift.subscription.MembershipTier
import ophan.thrift.subscription.SubscriptionType

enum class EventType(@JvmField val value: Int) {
    /**
     * When a user views a 'page'.
     */
    VIEW(0),

    /**
     * An ad has been loaded
     */
    AD_LOAD(1),

    /**
     * The event contains performance benchmark data.
     */
    PERFORMANCE(2),

    /**
     * The event contains profiling data for network based operations.
     */
    NETWORK(3),

    /**
     * The event contains information about an in-page interaction
     */
    INTERACTION(4),

    /**
     * The event contains information about the AB tests.
     */
    AB_TEST(5),

    /**
     * The event contains information about components.
     */
    COMPONENT_EVENT(6),

    /**
     * The event contains information about acquisitions.
     */
    ACQUISITION(7);

    companion object {
        @JvmStatic
        fun findByValue(value: Int): EventType? = when (value) {
            0 -> VIEW
            1 -> AD_LOAD
            2 -> PERFORMANCE
            3 -> NETWORK
            4 -> INTERACTION
            5 -> AB_TEST
            6 -> COMPONENT_EVENT
            7 -> ACQUISITION
            else -> null
        }
    }
}

/**
 * A specific version of the app exisits for each edition in the content api.
 * For example, US visitors get the US version of our app.
 */
enum class Edition(@JvmField val value: Int) {
    UK(0),

    US(1),

    AU(2),

    International(3);

    companion object {
        @JvmStatic
        fun findByValue(value: Int): Edition? = when (value) {
            0 -> UK
            1 -> US
            2 -> AU
            3 -> International
            else -> null
        }
    }
}

/**
 * The means by which the user arrived at a page.
 *
 * As well as the capitalised versions listed below, these values
 * can be supplied in json as camelCase, e.g. inAppLink
 */
enum class Source(@JvmField val value: Int) {
    /**
     * User clicked an link on a front or section page
     */
    FRONT_OR_SECTION(0),

    /**
     * user clicked a link on a fixtures page
     */
    FIXTURES_PAGE(1),

    /**
     * User swiped across the screen
     */
    SWIPE(2),

    /**
     * User clicked a link within an article
     */
    IN_ARTICLE_LINK(3),

    /**
     * Whether the user clicked on a Guardian link (anywhere on the device) and chose to open it using our native app.
     */
    EXTERNAL_LINK(4),

    /**
     * Whether the user clicked on a link in the 'More on this story' component.
     */
    RELATED_ARTICLE_LINK(5),

    /**
     * DEPRECATED - please use one of the other values begining with PUSH_
     * Whether the user came to the page via a push notification.
     * The id can be stored in Event.pushNotificationId.
     */
    PUSH(6),

    /**
     * meaning tbc
     */
    HANDOFF_WEB(7),

    /**
     * meaning tbc
     */
    HANDOFF_APP(8),

    /**
     * user clicked a link from a notification centre / home page widget
     */
    WIDGET(9),

    /**
     * meaning tbc
     */
    RESUME_MEDIA(10),

    /**
     * user clicked the back button
     */
    BACK(11),

    /**
     * meaning tbc
     */
    SEARCH(12),

    /**
     * Wheter the user clicked in iOs Spotlight and open the article in native app
     */
    SPOTLIGHT(13),

    /**
     * Indicates that the article was displayed automatically by the system during the state restoration process.
     */
    STATE_RESTORATION(14),

    /**
     * Whether the user came to the page via a breaking new push notification.
     */
    PUSH_BREAKING_NEWS(15),

    /**
     * Whether the user came to the page from a push notification that the user received because of following a contributor.
     */
    PUSH_FOLLOW_TAG(16),

    /**
     * Whether the user came to the page from a push notification that user received because of some other events.
     */
    PUSH_OTHER(17),

    /**
     * Whether the user came to the page from the Discover feature.
     */
    DISCOVER(18),

    /**
     * Whether the user came to the page from Membership content in their profile.
     */
    MEMBERSHIP(19),

    /**
     * Whether the user came to the page by opening the app from their own home screen.
     */
    HOME_SCREEN(20),

    /**
     * Whether the user came to the page by clicking on the navigation menu of the app
     */
    NAVIGATION(21);

    companion object {
        @JvmStatic
        fun findByValue(value: Int): Source? = when (value) {
            0 -> FRONT_OR_SECTION
            1 -> FIXTURES_PAGE
            2 -> SWIPE
            3 -> IN_ARTICLE_LINK
            4 -> EXTERNAL_LINK
            5 -> RELATED_ARTICLE_LINK
            6 -> PUSH
            7 -> HANDOFF_WEB
            8 -> HANDOFF_APP
            9 -> WIDGET
            10 -> RESUME_MEDIA
            11 -> BACK
            12 -> SEARCH
            13 -> SPOTLIGHT
            14 -> STATE_RESTORATION
            15 -> PUSH_BREAKING_NEWS
            16 -> PUSH_FOLLOW_TAG
            17 -> PUSH_OTHER
            18 -> DISCOVER
            19 -> MEMBERSHIP
            20 -> HOME_SCREEN
            21 -> NAVIGATION
            else -> null
        }
    }
}

data class ScrollDepth(
    @JvmField @ThriftField(fieldId = 1, isRequired = true) val maxExtent: Int,
    @JvmField @ThriftField(fieldId = 2, isOptional = true) val numberOfContainers: Int?,
    @JvmField @ThriftField(fieldId = 3, isOptional = true) val numberOfContainersViewed: Int?
) : Struct {
    override fun write(protocol: Protocol) {
        ADAPTER.write(protocol, this)
    }

    class Builder : StructBuilder<ScrollDepth> {
        private var maxExtent: Int? = null

        private var numberOfContainers: Int? = null

        private var numberOfContainersViewed: Int? = null

        constructor() {
            this.maxExtent = null
            this.numberOfContainers = null
            this.numberOfContainersViewed = null
        }

        constructor(source: ScrollDepth) {
            this.maxExtent = source.maxExtent
            this.numberOfContainers = source.numberOfContainers
            this.numberOfContainersViewed = source.numberOfContainersViewed
        }

        fun maxExtent(maxExtent: Int) = apply { this.maxExtent = maxExtent }

        fun numberOfContainers(numberOfContainers: Int?) = apply { this.numberOfContainers = numberOfContainers }

        fun numberOfContainersViewed(numberOfContainersViewed: Int?) = apply { this.numberOfContainersViewed = numberOfContainersViewed }

        override fun build(): ScrollDepth = ScrollDepth(maxExtent = checkNotNull(maxExtent) { "Required field 'maxExtent' is missing" },
                numberOfContainers = this.numberOfContainers,
                numberOfContainersViewed = this.numberOfContainersViewed)
        override fun reset() {
            this.maxExtent = null
            this.numberOfContainers = null
            this.numberOfContainersViewed = null
        }
    }

    private class ScrollDepthAdapter : Adapter<ScrollDepth, Builder> {
        override fun read(protocol: Protocol) = read(protocol, Builder())

        override fun read(protocol: Protocol, builder: Builder): ScrollDepth {
            protocol.readStructBegin()
            while (true) {
                val fieldMeta = protocol.readFieldBegin()
                if (fieldMeta.typeId == TType.STOP) {
                    break
                }
                when (fieldMeta.fieldId.toInt()) {
                    1 -> {
                        if (fieldMeta.typeId == TType.I32) {
                            val maxExtent = protocol.readI32()
                            builder.maxExtent(maxExtent)
                        } else {
                            ProtocolUtil.skip(protocol, fieldMeta.typeId)
                        }
                    }
                    2 -> {
                        if (fieldMeta.typeId == TType.I32) {
                            val numberOfContainers = protocol.readI32()
                            builder.numberOfContainers(numberOfContainers)
                        } else {
                            ProtocolUtil.skip(protocol, fieldMeta.typeId)
                        }
                    }
                    3 -> {
                        if (fieldMeta.typeId == TType.I32) {
                            val numberOfContainersViewed = protocol.readI32()
                            builder.numberOfContainersViewed(numberOfContainersViewed)
                        } else {
                            ProtocolUtil.skip(protocol, fieldMeta.typeId)
                        }
                    }
                    else -> ProtocolUtil.skip(protocol, fieldMeta.typeId)
                }
                protocol.readFieldEnd()
            }
            protocol.readStructEnd()
            return builder.build()
        }

        override fun write(protocol: Protocol, struct: ScrollDepth) {
            protocol.writeStructBegin("ScrollDepth")
            protocol.writeFieldBegin("maxExtent", 1, TType.I32)
            protocol.writeI32(struct.maxExtent)
            protocol.writeFieldEnd()
            if (struct.numberOfContainers != null) {
                protocol.writeFieldBegin("numberOfContainers", 2, TType.I32)
                protocol.writeI32(struct.numberOfContainers)
                protocol.writeFieldEnd()
            }
            if (struct.numberOfContainersViewed != null) {
                protocol.writeFieldBegin("numberOfContainersViewed", 3, TType.I32)
                protocol.writeI32(struct.numberOfContainersViewed)
                protocol.writeFieldEnd()
            }
            protocol.writeFieldStop()
            protocol.writeStructEnd()
        }
    }

    companion object {
        @JvmField
        val ADAPTER: Adapter<ScrollDepth, Builder> = ScrollDepthAdapter()
    }
}

/**
 * E.g. a 'page view' see EventType.
 */
data class Event(
    @JvmField @ThriftField(fieldId = 3, isOptional = true) val eventType: EventType? = ophan.thrift.nativeapp.EventType.VIEW,
    @JvmField @ThriftField(fieldId = 1, isRequired = true) val eventId: String,
    @JvmField @ThriftField(fieldId = 9, isOptional = true) val viewId: String?,
    @JvmField @ThriftField(fieldId = 22, isOptional = true) val ageMsLong: Long?,
    @JvmField @ThriftField(fieldId = 2, isOptional = true) val ageMs: Int? = 0,
    @JvmField @ThriftField(fieldId = 4, isOptional = true) val path: String?,
    @JvmField @ThriftField(fieldId = 5, isOptional = true) val previousPath: String?,
    @JvmField @ThriftField(fieldId = 6, isOptional = true) val referringSource: Source?,
    @JvmField @ThriftField(fieldId = 7, isOptional = true) val pushNotificationId: String?,
    @JvmField @ThriftField(fieldId = 8, isOptional = true) val adLoad: RenderedAd?,
    @JvmField @ThriftField(fieldId = 10, isOptional = true) val benchmark: BenchmarkData?,
    @JvmField @ThriftField(fieldId = 11, isOptional = true) val networkOperation: NetworkOperationData?,
    @JvmField @ThriftField(fieldId = 12, isOptional = true) val attentionMs: Long?,
    @JvmField @ThriftField(fieldId = 13, isOptional = true) val scrollDepth: ScrollDepth?,
    @JvmField @ThriftField(fieldId = 14, isOptional = true) val media: MediaPlayback?,
    @JvmField @ThriftField(fieldId = 15, isOptional = true) val ab: AbTestInfo?,
    @JvmField @ThriftField(fieldId = 16, isOptional = true) val interaction: Interaction?,
    @JvmField @ThriftField(fieldId = 17, isOptional = true) val referrer: Referrer?,
    @JvmField @ThriftField(fieldId = 18, isOptional = true) val url: Url?,
    @JvmField @ThriftField(fieldId = 19, isOptional = true) val renderedComponents: List<String>?,
    @JvmField @ThriftField(fieldId = 20, isOptional = true) val componentEvent: ComponentEvent?,
    @JvmField @ThriftField(fieldId = 21, isOptional = true) val acquisition: Acquisition?
) : Struct {
    override fun write(protocol: Protocol) {
        ADAPTER.write(protocol, this)
    }

    class Builder : StructBuilder<Event> {
        private var eventType: EventType? = ophan.thrift.nativeapp.EventType.VIEW

        private var eventId: String? = null

        private var viewId: String? = null

        private var ageMsLong: Long? = null

        private var ageMs: Int? = 0

        private var path: String? = null

        private var previousPath: String? = null

        private var referringSource: Source? = null

        private var pushNotificationId: String? = null

        private var adLoad: RenderedAd? = null

        private var benchmark: BenchmarkData? = null

        private var networkOperation: NetworkOperationData? = null

        private var attentionMs: Long? = null

        private var scrollDepth: ScrollDepth? = null

        private var media: MediaPlayback? = null

        private var ab: AbTestInfo? = null

        private var interaction: Interaction? = null

        private var referrer: Referrer? = null

        private var url: Url? = null

        private var renderedComponents: List<String>? = null

        private var componentEvent: ComponentEvent? = null

        private var acquisition: Acquisition? = null

        constructor() {
            this.eventType = ophan.thrift.nativeapp.EventType.VIEW
            this.eventId = null
            this.viewId = null
            this.ageMsLong = null
            this.ageMs = 0
            this.path = null
            this.previousPath = null
            this.referringSource = null
            this.pushNotificationId = null
            this.adLoad = null
            this.benchmark = null
            this.networkOperation = null
            this.attentionMs = null
            this.scrollDepth = null
            this.media = null
            this.ab = null
            this.interaction = null
            this.referrer = null
            this.url = null
            this.renderedComponents = null
            this.componentEvent = null
            this.acquisition = null
        }

        constructor(source: Event) {
            this.eventType = source.eventType
            this.eventId = source.eventId
            this.viewId = source.viewId
            this.ageMsLong = source.ageMsLong
            this.ageMs = source.ageMs
            this.path = source.path
            this.previousPath = source.previousPath
            this.referringSource = source.referringSource
            this.pushNotificationId = source.pushNotificationId
            this.adLoad = source.adLoad
            this.benchmark = source.benchmark
            this.networkOperation = source.networkOperation
            this.attentionMs = source.attentionMs
            this.scrollDepth = source.scrollDepth
            this.media = source.media
            this.ab = source.ab
            this.interaction = source.interaction
            this.referrer = source.referrer
            this.url = source.url
            this.renderedComponents = source.renderedComponents
            this.componentEvent = source.componentEvent
            this.acquisition = source.acquisition
        }

        fun eventType(eventType: EventType?) = apply { this.eventType = eventType }

        fun eventId(eventId: String) = apply { this.eventId = eventId }

        fun viewId(viewId: String?) = apply { this.viewId = viewId }

        fun ageMsLong(ageMsLong: Long?) = apply { this.ageMsLong = ageMsLong }

        fun ageMs(ageMs: Int?) = apply { this.ageMs = ageMs }

        fun path(path: String?) = apply { this.path = path }

        fun previousPath(previousPath: String?) = apply { this.previousPath = previousPath }

        fun referringSource(referringSource: Source?) = apply { this.referringSource = referringSource }

        fun pushNotificationId(pushNotificationId: String?) = apply { this.pushNotificationId = pushNotificationId }

        fun adLoad(adLoad: RenderedAd?) = apply { this.adLoad = adLoad }

        fun benchmark(benchmark: BenchmarkData?) = apply { this.benchmark = benchmark }

        fun networkOperation(networkOperation: NetworkOperationData?) = apply { this.networkOperation = networkOperation }

        fun attentionMs(attentionMs: Long?) = apply { this.attentionMs = attentionMs }

        fun scrollDepth(scrollDepth: ScrollDepth?) = apply { this.scrollDepth = scrollDepth }

        fun media(media: MediaPlayback?) = apply { this.media = media }

        fun ab(ab: AbTestInfo?) = apply { this.ab = ab }

        fun interaction(interaction: Interaction?) = apply { this.interaction = interaction }

        fun referrer(referrer: Referrer?) = apply { this.referrer = referrer }

        fun url(url: Url?) = apply { this.url = url }

        fun renderedComponents(renderedComponents: List<String>?) = apply { this.renderedComponents = renderedComponents }

        fun componentEvent(componentEvent: ComponentEvent?) = apply { this.componentEvent = componentEvent }

        fun acquisition(acquisition: Acquisition?) = apply { this.acquisition = acquisition }

        override fun build(): Event = Event(eventType = this.eventType,
                eventId = checkNotNull(eventId) { "Required field 'eventId' is missing" },
                viewId = this.viewId, ageMsLong = this.ageMsLong, ageMs = this.ageMs,
                path = this.path, previousPath = this.previousPath,
                referringSource = this.referringSource,
                pushNotificationId = this.pushNotificationId, adLoad = this.adLoad,
                benchmark = this.benchmark, networkOperation = this.networkOperation,
                attentionMs = this.attentionMs, scrollDepth = this.scrollDepth, media = this.media,
                ab = this.ab, interaction = this.interaction, referrer = this.referrer,
                url = this.url, renderedComponents = this.renderedComponents,
                componentEvent = this.componentEvent, acquisition = this.acquisition)
        override fun reset() {
            this.eventType = ophan.thrift.nativeapp.EventType.VIEW
            this.eventId = null
            this.viewId = null
            this.ageMsLong = null
            this.ageMs = 0
            this.path = null
            this.previousPath = null
            this.referringSource = null
            this.pushNotificationId = null
            this.adLoad = null
            this.benchmark = null
            this.networkOperation = null
            this.attentionMs = null
            this.scrollDepth = null
            this.media = null
            this.ab = null
            this.interaction = null
            this.referrer = null
            this.url = null
            this.renderedComponents = null
            this.componentEvent = null
            this.acquisition = null
        }
    }

    private class EventAdapter : Adapter<Event, Builder> {
        override fun read(protocol: Protocol) = read(protocol, Builder())

        override fun read(protocol: Protocol, builder: Builder): Event {
            protocol.readStructBegin()
            while (true) {
                val fieldMeta = protocol.readFieldBegin()
                if (fieldMeta.typeId == TType.STOP) {
                    break
                }
                when (fieldMeta.fieldId.toInt()) {
                    3 -> {
                        if (fieldMeta.typeId == TType.I32) {
                            val eventType = protocol.readI32().let {
                                EventType.findByValue(it) ?: throw ThriftException(ThriftException.Kind.PROTOCOL_ERROR, "Unexpected value for enum type EventType: $it")
                            }
                            builder.eventType(eventType)
                        } else {
                            ProtocolUtil.skip(protocol, fieldMeta.typeId)
                        }
                    }
                    1 -> {
                        if (fieldMeta.typeId == TType.STRING) {
                            val eventId = protocol.readString()
                            builder.eventId(eventId)
                        } else {
                            ProtocolUtil.skip(protocol, fieldMeta.typeId)
                        }
                    }
                    9 -> {
                        if (fieldMeta.typeId == TType.STRING) {
                            val viewId = protocol.readString()
                            builder.viewId(viewId)
                        } else {
                            ProtocolUtil.skip(protocol, fieldMeta.typeId)
                        }
                    }
                    22 -> {
                        if (fieldMeta.typeId == TType.I64) {
                            val ageMsLong = protocol.readI64()
                            builder.ageMsLong(ageMsLong)
                        } else {
                            ProtocolUtil.skip(protocol, fieldMeta.typeId)
                        }
                    }
                    2 -> {
                        if (fieldMeta.typeId == TType.I32) {
                            val ageMs = protocol.readI32()
                            builder.ageMs(ageMs)
                        } else {
                            ProtocolUtil.skip(protocol, fieldMeta.typeId)
                        }
                    }
                    4 -> {
                        if (fieldMeta.typeId == TType.STRING) {
                            val path = protocol.readString()
                            builder.path(path)
                        } else {
                            ProtocolUtil.skip(protocol, fieldMeta.typeId)
                        }
                    }
                    5 -> {
                        if (fieldMeta.typeId == TType.STRING) {
                            val previousPath = protocol.readString()
                            builder.previousPath(previousPath)
                        } else {
                            ProtocolUtil.skip(protocol, fieldMeta.typeId)
                        }
                    }
                    6 -> {
                        if (fieldMeta.typeId == TType.I32) {
                            val referringSource = protocol.readI32().let {
                                Source.findByValue(it) ?: throw ThriftException(ThriftException.Kind.PROTOCOL_ERROR, "Unexpected value for enum type Source: $it")
                            }
                            builder.referringSource(referringSource)
                        } else {
                            ProtocolUtil.skip(protocol, fieldMeta.typeId)
                        }
                    }
                    7 -> {
                        if (fieldMeta.typeId == TType.STRING) {
                            val pushNotificationId = protocol.readString()
                            builder.pushNotificationId(pushNotificationId)
                        } else {
                            ProtocolUtil.skip(protocol, fieldMeta.typeId)
                        }
                    }
                    8 -> {
                        if (fieldMeta.typeId == TType.STRUCT) {
                            val adLoad = RenderedAd.ADAPTER.read(protocol)
                            builder.adLoad(adLoad)
                        } else {
                            ProtocolUtil.skip(protocol, fieldMeta.typeId)
                        }
                    }
                    10 -> {
                        if (fieldMeta.typeId == TType.STRUCT) {
                            val benchmark = BenchmarkData.ADAPTER.read(protocol)
                            builder.benchmark(benchmark)
                        } else {
                            ProtocolUtil.skip(protocol, fieldMeta.typeId)
                        }
                    }
                    11 -> {
                        if (fieldMeta.typeId == TType.STRUCT) {
                            val networkOperation = NetworkOperationData.ADAPTER.read(protocol)
                            builder.networkOperation(networkOperation)
                        } else {
                            ProtocolUtil.skip(protocol, fieldMeta.typeId)
                        }
                    }
                    12 -> {
                        if (fieldMeta.typeId == TType.I64) {
                            val attentionMs = protocol.readI64()
                            builder.attentionMs(attentionMs)
                        } else {
                            ProtocolUtil.skip(protocol, fieldMeta.typeId)
                        }
                    }
                    13 -> {
                        if (fieldMeta.typeId == TType.STRUCT) {
                            val scrollDepth = ScrollDepth.ADAPTER.read(protocol)
                            builder.scrollDepth(scrollDepth)
                        } else {
                            ProtocolUtil.skip(protocol, fieldMeta.typeId)
                        }
                    }
                    14 -> {
                        if (fieldMeta.typeId == TType.STRUCT) {
                            val media = MediaPlayback.ADAPTER.read(protocol)
                            builder.media(media)
                        } else {
                            ProtocolUtil.skip(protocol, fieldMeta.typeId)
                        }
                    }
                    15 -> {
                        if (fieldMeta.typeId == TType.STRUCT) {
                            val ab = AbTestInfo.ADAPTER.read(protocol)
                            builder.ab(ab)
                        } else {
                            ProtocolUtil.skip(protocol, fieldMeta.typeId)
                        }
                    }
                    16 -> {
                        if (fieldMeta.typeId == TType.STRUCT) {
                            val interaction = Interaction.ADAPTER.read(protocol)
                            builder.interaction(interaction)
                        } else {
                            ProtocolUtil.skip(protocol, fieldMeta.typeId)
                        }
                    }
                    17 -> {
                        if (fieldMeta.typeId == TType.STRUCT) {
                            val referrer = Referrer.ADAPTER.read(protocol)
                            builder.referrer(referrer)
                        } else {
                            ProtocolUtil.skip(protocol, fieldMeta.typeId)
                        }
                    }
                    18 -> {
                        if (fieldMeta.typeId == TType.STRUCT) {
                            val url = Url.ADAPTER.read(protocol)
                            builder.url(url)
                        } else {
                            ProtocolUtil.skip(protocol, fieldMeta.typeId)
                        }
                    }
                    19 -> {
                        if (fieldMeta.typeId == TType.LIST) {
                            val list0 = protocol.readListBegin()
                            val renderedComponents = ArrayList<String>(list0.size)
                            for (i0 in 0 until list0.size) {
                                val item0 = protocol.readString()
                                renderedComponents += item0
                            }
                            protocol.readListEnd()
                            builder.renderedComponents(renderedComponents)
                        } else {
                            ProtocolUtil.skip(protocol, fieldMeta.typeId)
                        }
                    }
                    20 -> {
                        if (fieldMeta.typeId == TType.STRUCT) {
                            val componentEvent = ComponentEvent.ADAPTER.read(protocol)
                            builder.componentEvent(componentEvent)
                        } else {
                            ProtocolUtil.skip(protocol, fieldMeta.typeId)
                        }
                    }
                    21 -> {
                        if (fieldMeta.typeId == TType.STRUCT) {
                            val acquisition = Acquisition.ADAPTER.read(protocol)
                            builder.acquisition(acquisition)
                        } else {
                            ProtocolUtil.skip(protocol, fieldMeta.typeId)
                        }
                    }
                    else -> ProtocolUtil.skip(protocol, fieldMeta.typeId)
                }
                protocol.readFieldEnd()
            }
            protocol.readStructEnd()
            return builder.build()
        }

        override fun write(protocol: Protocol, struct: Event) {
            protocol.writeStructBegin("Event")
            if (struct.eventType != null) {
                protocol.writeFieldBegin("eventType", 3, TType.I32)
                protocol.writeI32(struct.eventType.value)
                protocol.writeFieldEnd()
            }
            protocol.writeFieldBegin("eventId", 1, TType.STRING)
            protocol.writeString(struct.eventId)
            protocol.writeFieldEnd()
            if (struct.viewId != null) {
                protocol.writeFieldBegin("viewId", 9, TType.STRING)
                protocol.writeString(struct.viewId)
                protocol.writeFieldEnd()
            }
            if (struct.ageMsLong != null) {
                protocol.writeFieldBegin("ageMsLong", 22, TType.I64)
                protocol.writeI64(struct.ageMsLong)
                protocol.writeFieldEnd()
            }
            if (struct.ageMs != null) {
                protocol.writeFieldBegin("ageMs", 2, TType.I32)
                protocol.writeI32(struct.ageMs)
                protocol.writeFieldEnd()
            }
            if (struct.path != null) {
                protocol.writeFieldBegin("path", 4, TType.STRING)
                protocol.writeString(struct.path)
                protocol.writeFieldEnd()
            }
            if (struct.previousPath != null) {
                protocol.writeFieldBegin("previousPath", 5, TType.STRING)
                protocol.writeString(struct.previousPath)
                protocol.writeFieldEnd()
            }
            if (struct.referringSource != null) {
                protocol.writeFieldBegin("referringSource", 6, TType.I32)
                protocol.writeI32(struct.referringSource.value)
                protocol.writeFieldEnd()
            }
            if (struct.pushNotificationId != null) {
                protocol.writeFieldBegin("pushNotificationId", 7, TType.STRING)
                protocol.writeString(struct.pushNotificationId)
                protocol.writeFieldEnd()
            }
            if (struct.adLoad != null) {
                protocol.writeFieldBegin("adLoad", 8, TType.STRUCT)
                RenderedAd.ADAPTER.write(protocol, struct.adLoad)
                protocol.writeFieldEnd()
            }
            if (struct.benchmark != null) {
                protocol.writeFieldBegin("benchmark", 10, TType.STRUCT)
                BenchmarkData.ADAPTER.write(protocol, struct.benchmark)
                protocol.writeFieldEnd()
            }
            if (struct.networkOperation != null) {
                protocol.writeFieldBegin("networkOperation", 11, TType.STRUCT)
                NetworkOperationData.ADAPTER.write(protocol, struct.networkOperation)
                protocol.writeFieldEnd()
            }
            if (struct.attentionMs != null) {
                protocol.writeFieldBegin("attentionMs", 12, TType.I64)
                protocol.writeI64(struct.attentionMs)
                protocol.writeFieldEnd()
            }
            if (struct.scrollDepth != null) {
                protocol.writeFieldBegin("scrollDepth", 13, TType.STRUCT)
                ScrollDepth.ADAPTER.write(protocol, struct.scrollDepth)
                protocol.writeFieldEnd()
            }
            if (struct.media != null) {
                protocol.writeFieldBegin("media", 14, TType.STRUCT)
                MediaPlayback.ADAPTER.write(protocol, struct.media)
                protocol.writeFieldEnd()
            }
            if (struct.ab != null) {
                protocol.writeFieldBegin("ab", 15, TType.STRUCT)
                AbTestInfo.ADAPTER.write(protocol, struct.ab)
                protocol.writeFieldEnd()
            }
            if (struct.interaction != null) {
                protocol.writeFieldBegin("interaction", 16, TType.STRUCT)
                Interaction.ADAPTER.write(protocol, struct.interaction)
                protocol.writeFieldEnd()
            }
            if (struct.referrer != null) {
                protocol.writeFieldBegin("referrer", 17, TType.STRUCT)
                Referrer.ADAPTER.write(protocol, struct.referrer)
                protocol.writeFieldEnd()
            }
            if (struct.url != null) {
                protocol.writeFieldBegin("url", 18, TType.STRUCT)
                Url.ADAPTER.write(protocol, struct.url)
                protocol.writeFieldEnd()
            }
            if (struct.renderedComponents != null) {
                protocol.writeFieldBegin("renderedComponents", 19, TType.LIST)
                protocol.writeListBegin(TType.STRING, struct.renderedComponents.size)
                for (item0 in struct.renderedComponents) {
                    protocol.writeString(item0)
                }
                protocol.writeListEnd()
                protocol.writeFieldEnd()
            }
            if (struct.componentEvent != null) {
                protocol.writeFieldBegin("componentEvent", 20, TType.STRUCT)
                ComponentEvent.ADAPTER.write(protocol, struct.componentEvent)
                protocol.writeFieldEnd()
            }
            if (struct.acquisition != null) {
                protocol.writeFieldBegin("acquisition", 21, TType.STRUCT)
                Acquisition.ADAPTER.write(protocol, struct.acquisition)
                protocol.writeFieldEnd()
            }
            protocol.writeFieldStop()
            protocol.writeStructEnd()
        }
    }

    companion object {
        @JvmField
        val ADAPTER: Adapter<Event, Builder> = EventAdapter()
    }
}

/**
 * Details about this running application
 */
data class App(
    @JvmField @ThriftField(fieldId = 1, isOptional = true) val version: String?,
    @JvmField @ThriftField(fieldId = 2, isOptional = true) val family: String?,
    @JvmField @ThriftField(fieldId = 3, isOptional = true) val os: String?,
    @JvmField @ThriftField(fieldId = 4, isOptional = true) val edition: Edition?
) : Struct {
    override fun write(protocol: Protocol) {
        ADAPTER.write(protocol, this)
    }

    class Builder : StructBuilder<App> {
        private var version: String? = null

        private var family: String? = null

        private var os: String? = null

        private var edition: Edition? = null

        constructor() {
            this.version = null
            this.family = null
            this.os = null
            this.edition = null
        }

        constructor(source: App) {
            this.version = source.version
            this.family = source.family
            this.os = source.os
            this.edition = source.edition
        }

        fun version(version: String?) = apply { this.version = version }

        fun family(family: String?) = apply { this.family = family }

        fun os(os: String?) = apply { this.os = os }

        fun edition(edition: Edition?) = apply { this.edition = edition }

        override fun build(): App = App(version = this.version, family = this.family, os = this.os,
                edition = this.edition)
        override fun reset() {
            this.version = null
            this.family = null
            this.os = null
            this.edition = null
        }
    }

    private class AppAdapter : Adapter<App, Builder> {
        override fun read(protocol: Protocol) = read(protocol, Builder())

        override fun read(protocol: Protocol, builder: Builder): App {
            protocol.readStructBegin()
            while (true) {
                val fieldMeta = protocol.readFieldBegin()
                if (fieldMeta.typeId == TType.STOP) {
                    break
                }
                when (fieldMeta.fieldId.toInt()) {
                    1 -> {
                        if (fieldMeta.typeId == TType.STRING) {
                            val version = protocol.readString()
                            builder.version(version)
                        } else {
                            ProtocolUtil.skip(protocol, fieldMeta.typeId)
                        }
                    }
                    2 -> {
                        if (fieldMeta.typeId == TType.STRING) {
                            val family = protocol.readString()
                            builder.family(family)
                        } else {
                            ProtocolUtil.skip(protocol, fieldMeta.typeId)
                        }
                    }
                    3 -> {
                        if (fieldMeta.typeId == TType.STRING) {
                            val os = protocol.readString()
                            builder.os(os)
                        } else {
                            ProtocolUtil.skip(protocol, fieldMeta.typeId)
                        }
                    }
                    4 -> {
                        if (fieldMeta.typeId == TType.I32) {
                            val edition = protocol.readI32().let {
                                Edition.findByValue(it) ?: throw ThriftException(ThriftException.Kind.PROTOCOL_ERROR, "Unexpected value for enum type Edition: $it")
                            }
                            builder.edition(edition)
                        } else {
                            ProtocolUtil.skip(protocol, fieldMeta.typeId)
                        }
                    }
                    else -> ProtocolUtil.skip(protocol, fieldMeta.typeId)
                }
                protocol.readFieldEnd()
            }
            protocol.readStructEnd()
            return builder.build()
        }

        override fun write(protocol: Protocol, struct: App) {
            protocol.writeStructBegin("App")
            if (struct.version != null) {
                protocol.writeFieldBegin("version", 1, TType.STRING)
                protocol.writeString(struct.version)
                protocol.writeFieldEnd()
            }
            if (struct.family != null) {
                protocol.writeFieldBegin("family", 2, TType.STRING)
                protocol.writeString(struct.family)
                protocol.writeFieldEnd()
            }
            if (struct.os != null) {
                protocol.writeFieldBegin("os", 3, TType.STRING)
                protocol.writeString(struct.os)
                protocol.writeFieldEnd()
            }
            if (struct.edition != null) {
                protocol.writeFieldBegin("edition", 4, TType.I32)
                protocol.writeI32(struct.edition.value)
                protocol.writeFieldEnd()
            }
            protocol.writeFieldStop()
            protocol.writeStructEnd()
        }
    }

    companion object {
        @JvmField
        val ADAPTER: Adapter<App, Builder> = AppAdapter()
    }
}

data class Device(@JvmField @ThriftField(fieldId = 1, isOptional = true) val name: String?, @JvmField @ThriftField(fieldId = 2, isOptional = true) val manufacturer: String?) : Struct {
    override fun write(protocol: Protocol) {
        ADAPTER.write(protocol, this)
    }

    class Builder : StructBuilder<Device> {
        private var name: String? = null

        private var manufacturer: String? = null

        constructor() {
            this.name = null
            this.manufacturer = null
        }

        constructor(source: Device) {
            this.name = source.name
            this.manufacturer = source.manufacturer
        }

        fun name(name: String?) = apply { this.name = name }

        fun manufacturer(manufacturer: String?) = apply { this.manufacturer = manufacturer }

        override fun build(): Device = Device(name = this.name, manufacturer = this.manufacturer)
        override fun reset() {
            this.name = null
            this.manufacturer = null
        }
    }

    private class DeviceAdapter : Adapter<Device, Builder> {
        override fun read(protocol: Protocol) = read(protocol, Builder())

        override fun read(protocol: Protocol, builder: Builder): Device {
            protocol.readStructBegin()
            while (true) {
                val fieldMeta = protocol.readFieldBegin()
                if (fieldMeta.typeId == TType.STOP) {
                    break
                }
                when (fieldMeta.fieldId.toInt()) {
                    1 -> {
                        if (fieldMeta.typeId == TType.STRING) {
                            val name = protocol.readString()
                            builder.name(name)
                        } else {
                            ProtocolUtil.skip(protocol, fieldMeta.typeId)
                        }
                    }
                    2 -> {
                        if (fieldMeta.typeId == TType.STRING) {
                            val manufacturer = protocol.readString()
                            builder.manufacturer(manufacturer)
                        } else {
                            ProtocolUtil.skip(protocol, fieldMeta.typeId)
                        }
                    }
                    else -> ProtocolUtil.skip(protocol, fieldMeta.typeId)
                }
                protocol.readFieldEnd()
            }
            protocol.readStructEnd()
            return builder.build()
        }

        override fun write(protocol: Protocol, struct: Device) {
            protocol.writeStructBegin("Device")
            if (struct.name != null) {
                protocol.writeFieldBegin("name", 1, TType.STRING)
                protocol.writeString(struct.name)
                protocol.writeFieldEnd()
            }
            if (struct.manufacturer != null) {
                protocol.writeFieldBegin("manufacturer", 2, TType.STRING)
                protocol.writeString(struct.manufacturer)
                protocol.writeFieldEnd()
            }
            protocol.writeFieldStop()
            protocol.writeStructEnd()
        }
    }

    companion object {
        @JvmField
        val ADAPTER: Adapter<Device, Builder> = DeviceAdapter()
    }
}

/**
 * This is the root object that represents a tracking submission from native apps.
 *
 * This can be supplied to ophan in one of two ways:
 *
 * <ol>
 *   <li>Create the equivalent json and POST the json to https://ophan.theguardian.com/mob with a content type
 *   of application/json</li>
 *   <li>Create a thift binary blob in compact binary protocol format from
 *   <a href="https://github.com/guardian/ophan/blob/master/event-model/src/main/thrift/nativeapp.thrift">this definition</a>
 *   and POST to
 *   https://ophan.theguardian.com/mob a content type of application/vnd.apache.thrift.compact</li>
 * </ol>
 *
 * Note that, for largely backwards compatibility reasons, in some cases we allow synonyms for enum values in
 * json; these are noted in the descriptions below.
 */
data class NativeAppSubmission(
    @JvmField @ThriftField(fieldId = 2, isRequired = true) val app: App,
    @JvmField @ThriftField(fieldId = 3, isOptional = true) val device: Device?,
    @JvmField @ThriftField(fieldId = 4, isRequired = true) val deviceId: String,
    @JvmField @ThriftField(fieldId = 5, isOptional = true) val userId: String?,
    @JvmField @ThriftField(fieldId = 8, isOptional = true) val kruxId: String?,
    @JvmField @ThriftField(fieldId = 6, isOptional = true) val subscriptionId: SubscriptionType?,
    @JvmField @ThriftField(fieldId = 7, isRequired = true) val events: List<Event>,
    @JvmField @ThriftField(fieldId = 9, isOptional = true) val membershipTier: MembershipTier?
) : Struct {
    override fun write(protocol: Protocol) {
        ADAPTER.write(protocol, this)
    }

    class Builder : StructBuilder<NativeAppSubmission> {
        private var app: App? = null

        private var device: Device? = null

        private var deviceId: String? = null

        private var userId: String? = null

        private var kruxId: String? = null

        private var subscriptionId: SubscriptionType? = null

        private var events: List<Event>? = null

        private var membershipTier: MembershipTier? = null

        constructor() {
            this.app = null
            this.device = null
            this.deviceId = null
            this.userId = null
            this.kruxId = null
            this.subscriptionId = null
            this.events = null
            this.membershipTier = null
        }

        constructor(source: NativeAppSubmission) {
            this.app = source.app
            this.device = source.device
            this.deviceId = source.deviceId
            this.userId = source.userId
            this.kruxId = source.kruxId
            this.subscriptionId = source.subscriptionId
            this.events = source.events
            this.membershipTier = source.membershipTier
        }

        fun app(app: App) = apply { this.app = app }

        fun device(device: Device?) = apply { this.device = device }

        fun deviceId(deviceId: String) = apply { this.deviceId = deviceId }

        fun userId(userId: String?) = apply { this.userId = userId }

        fun kruxId(kruxId: String?) = apply { this.kruxId = kruxId }

        fun subscriptionId(subscriptionId: SubscriptionType?) = apply { this.subscriptionId = subscriptionId }

        fun events(events: List<Event>) = apply { this.events = events }

        fun membershipTier(membershipTier: MembershipTier?) = apply { this.membershipTier = membershipTier }

        override fun build(): NativeAppSubmission = NativeAppSubmission(app = checkNotNull(app) { "Required field 'app' is missing" },
                device = this.device,
                deviceId = checkNotNull(deviceId) { "Required field 'deviceId' is missing" },
                userId = this.userId, kruxId = this.kruxId, subscriptionId = this.subscriptionId,
                events = checkNotNull(events) { "Required field 'events' is missing" },
                membershipTier = this.membershipTier)
        override fun reset() {
            this.app = null
            this.device = null
            this.deviceId = null
            this.userId = null
            this.kruxId = null
            this.subscriptionId = null
            this.events = null
            this.membershipTier = null
        }
    }

    private class NativeAppSubmissionAdapter : Adapter<NativeAppSubmission, Builder> {
        override fun read(protocol: Protocol) = read(protocol, Builder())

        override fun read(protocol: Protocol, builder: Builder): NativeAppSubmission {
            protocol.readStructBegin()
            while (true) {
                val fieldMeta = protocol.readFieldBegin()
                if (fieldMeta.typeId == TType.STOP) {
                    break
                }
                when (fieldMeta.fieldId.toInt()) {
                    2 -> {
                        if (fieldMeta.typeId == TType.STRUCT) {
                            val app = App.ADAPTER.read(protocol)
                            builder.app(app)
                        } else {
                            ProtocolUtil.skip(protocol, fieldMeta.typeId)
                        }
                    }
                    3 -> {
                        if (fieldMeta.typeId == TType.STRUCT) {
                            val device = Device.ADAPTER.read(protocol)
                            builder.device(device)
                        } else {
                            ProtocolUtil.skip(protocol, fieldMeta.typeId)
                        }
                    }
                    4 -> {
                        if (fieldMeta.typeId == TType.STRING) {
                            val deviceId = protocol.readString()
                            builder.deviceId(deviceId)
                        } else {
                            ProtocolUtil.skip(protocol, fieldMeta.typeId)
                        }
                    }
                    5 -> {
                        if (fieldMeta.typeId == TType.STRING) {
                            val userId = protocol.readString()
                            builder.userId(userId)
                        } else {
                            ProtocolUtil.skip(protocol, fieldMeta.typeId)
                        }
                    }
                    8 -> {
                        if (fieldMeta.typeId == TType.STRING) {
                            val kruxId = protocol.readString()
                            builder.kruxId(kruxId)
                        } else {
                            ProtocolUtil.skip(protocol, fieldMeta.typeId)
                        }
                    }
                    6 -> {
                        if (fieldMeta.typeId == TType.I32) {
                            val subscriptionId = protocol.readI32().let {
                                SubscriptionType.findByValue(it) ?: throw ThriftException(ThriftException.Kind.PROTOCOL_ERROR, "Unexpected value for enum type SubscriptionType: $it")
                            }
                            builder.subscriptionId(subscriptionId)
                        } else {
                            ProtocolUtil.skip(protocol, fieldMeta.typeId)
                        }
                    }
                    7 -> {
                        if (fieldMeta.typeId == TType.LIST) {
                            val list0 = protocol.readListBegin()
                            val events = ArrayList<Event>(list0.size)
                            for (i0 in 0 until list0.size) {
                                val item0 = Event.ADAPTER.read(protocol)
                                events += item0
                            }
                            protocol.readListEnd()
                            builder.events(events)
                        } else {
                            ProtocolUtil.skip(protocol, fieldMeta.typeId)
                        }
                    }
                    9 -> {
                        if (fieldMeta.typeId == TType.I32) {
                            val membershipTier = protocol.readI32().let {
                                MembershipTier.findByValue(it) ?: throw ThriftException(ThriftException.Kind.PROTOCOL_ERROR, "Unexpected value for enum type MembershipTier: $it")
                            }
                            builder.membershipTier(membershipTier)
                        } else {
                            ProtocolUtil.skip(protocol, fieldMeta.typeId)
                        }
                    }
                    else -> ProtocolUtil.skip(protocol, fieldMeta.typeId)
                }
                protocol.readFieldEnd()
            }
            protocol.readStructEnd()
            return builder.build()
        }

        override fun write(protocol: Protocol, struct: NativeAppSubmission) {
            protocol.writeStructBegin("NativeAppSubmission")
            protocol.writeFieldBegin("app", 2, TType.STRUCT)
            App.ADAPTER.write(protocol, struct.app)
            protocol.writeFieldEnd()
            if (struct.device != null) {
                protocol.writeFieldBegin("device", 3, TType.STRUCT)
                Device.ADAPTER.write(protocol, struct.device)
                protocol.writeFieldEnd()
            }
            protocol.writeFieldBegin("deviceId", 4, TType.STRING)
            protocol.writeString(struct.deviceId)
            protocol.writeFieldEnd()
            if (struct.userId != null) {
                protocol.writeFieldBegin("userId", 5, TType.STRING)
                protocol.writeString(struct.userId)
                protocol.writeFieldEnd()
            }
            if (struct.kruxId != null) {
                protocol.writeFieldBegin("kruxId", 8, TType.STRING)
                protocol.writeString(struct.kruxId)
                protocol.writeFieldEnd()
            }
            if (struct.subscriptionId != null) {
                protocol.writeFieldBegin("subscriptionId", 6, TType.I32)
                protocol.writeI32(struct.subscriptionId.value)
                protocol.writeFieldEnd()
            }
            protocol.writeFieldBegin("events", 7, TType.LIST)
            protocol.writeListBegin(TType.STRUCT, struct.events.size)
            for (item0 in struct.events) {
                Event.ADAPTER.write(protocol, item0)
            }
            protocol.writeListEnd()
            protocol.writeFieldEnd()
            if (struct.membershipTier != null) {
                protocol.writeFieldBegin("membershipTier", 9, TType.I32)
                protocol.writeI32(struct.membershipTier.value)
                protocol.writeFieldEnd()
            }
            protocol.writeFieldStop()
            protocol.writeStructEnd()
        }
    }

    companion object {
        @JvmField
        val ADAPTER: Adapter<NativeAppSubmission, Builder> = NativeAppSubmissionAdapter()
    }
}
