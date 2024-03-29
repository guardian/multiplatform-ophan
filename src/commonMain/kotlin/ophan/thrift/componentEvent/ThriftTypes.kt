package ophan.thrift.componentEvent

import com.microsoft.thrifty.Adapter
import com.microsoft.thrifty.Struct
import com.microsoft.thrifty.StructBuilder
import com.microsoft.thrifty.TType
import com.microsoft.thrifty.ThriftException
import com.microsoft.thrifty.ThriftField
import com.microsoft.thrifty.protocol.Protocol
import com.microsoft.thrifty.util.ProtocolUtil
import kotlin.Int
import kotlin.String
import kotlin.collections.LinkedHashSet
import kotlin.collections.Set
import kotlin.jvm.JvmField
import kotlin.jvm.JvmStatic
import ophan.thrift.event.AbTest
import ophan.thrift.event.Product

/**
 * Specific actions that can be taken against a Component.
 *
 * Aim to use one of the existing entries here, or create a new one. The examples in the comments
 * are not intended to be exhaustive.
 *
 * If a general "click" event is enough, consider using the "value" field in ComponentEvent
 * to hold any additional information relating to the click.
 */
enum class Action(@JvmField val value: Int) {
    /**
     * The component was inserted into its container (e.g. a web page or view in an app)
     */
    INSERT(1),

    /**
     * The component was in view on screen
     */
    VIEW(2),

    /**
     * The component was expanded (e.g. "see more").
     * Not a navigation away to another page
     */
    EXPAND(3),

    /**
     * A "like", thumbs up, etc.
     */
    LIKE(4),

    /**
     * A "dislike", thumbs down, etc.
     */
    DISLIKE(5),

    /**
     * A subscription to a service, newsletter, etc.
     */
    SUBSCRIBE(6),

    /**
     * Selection of an answer in a quiz
     */
    ANSWER(7),

    /**
     * A vote in a poll
     */
    VOTE(8),

    /**
     * A single click on the component which is not covered by
     * any of the existing Actions.
     */
    CLICK(9),

    /**
     * User sign in to Guardian Identity account
     */
    SIGN_IN(10),

    /**
     * User creates a Guardian Identity account
     */
    CREATE_ACCOUNT(11);

    companion object {
        @JvmStatic
        fun findByValue(value: Int): Action? = when (value) {
            1 -> INSERT
            2 -> VIEW
            3 -> EXPAND
            4 -> LIKE
            5 -> DISLIKE
            6 -> SUBSCRIBE
            7 -> ANSWER
            8 -> VOTE
            9 -> CLICK
            10 -> SIGN_IN
            11 -> CREATE_ACCOUNT
            else -> null
        }
    }
}

/**
 * The different types of component that can be rendered
 */
enum class ComponentType(@JvmField val value: Int) {
    READERS_QUESTIONS_ATOM(1),

    QANDA_ATOM(2),

    PROFILE_ATOM(3),

    GUIDE_ATOM(4),

    TIMELINE_ATOM(5),

    NEWSLETTER_SUBSCRIPTION(6),

    SURVEYS_QUESTIONS(7),

    ACQUISITIONS_EPIC(8),

    ACQUISITIONS_ENGAGEMENT_BANNER(9),

    ACQUISITIONS_THANK_YOU_EPIC(10),

    ACQUISITIONS_HEADER(11),

    ACQUISITIONS_FOOTER(12),

    ACQUISITIONS_INTERACTIVE_SLICE(13),

    ACQUISITIONS_NUGGET(14),

    ACQUISITIONS_STANDFIRST(15),

    ACQUISITIONS_THRASHER(16),

    ACQUISITIONS_EDITORIAL_LINK(17),

    ACQUISITIONS_MANAGE_MY_ACCOUNT(18),

    ACQUISITIONS_BUTTON(19),

    ACQUISITIONS_OTHER(20),

    APP_ADVERT(21),

    APP_AUDIO(22),

    APP_BUTTON(23),

    APP_CARD(24),

    APP_CROSSWORDS(25),

    APP_ENGAGEMENT_BANNER(26),

    APP_EPIC(27),

    APP_GALLERY(28),

    APP_LINK(29),

    APP_NAVIGATION_ITEM(30),

    APP_SCREEN(31),

    APP_THRASHER(32),

    APP_VIDEO(33),

    AUDIO_ATOM(34),

    CHART_ATOM(35),

    ACQUISITIONS_MERCHANDISING(36),

    ACQUISITIONS_HOUSE_ADS(37),

    SIGN_IN_GATE(38),

    ACQUISITIONS_SUBSCRIPTIONS_BANNER(39),

    MOBILE_STICKY_AD(40),

    IDENTITY_AUTHENTICATION(41),

    RETENTION_ENGAGEMENT_BANNER(42),

    ACQUISITION_SUPPORT_SITE(43),

    RETENTION_EPIC(44);

    companion object {
        @JvmStatic
        fun findByValue(value: Int): ComponentType? = when (value) {
            1 -> READERS_QUESTIONS_ATOM
            2 -> QANDA_ATOM
            3 -> PROFILE_ATOM
            4 -> GUIDE_ATOM
            5 -> TIMELINE_ATOM
            6 -> NEWSLETTER_SUBSCRIPTION
            7 -> SURVEYS_QUESTIONS
            8 -> ACQUISITIONS_EPIC
            9 -> ACQUISITIONS_ENGAGEMENT_BANNER
            10 -> ACQUISITIONS_THANK_YOU_EPIC
            11 -> ACQUISITIONS_HEADER
            12 -> ACQUISITIONS_FOOTER
            13 -> ACQUISITIONS_INTERACTIVE_SLICE
            14 -> ACQUISITIONS_NUGGET
            15 -> ACQUISITIONS_STANDFIRST
            16 -> ACQUISITIONS_THRASHER
            17 -> ACQUISITIONS_EDITORIAL_LINK
            18 -> ACQUISITIONS_MANAGE_MY_ACCOUNT
            19 -> ACQUISITIONS_BUTTON
            20 -> ACQUISITIONS_OTHER
            21 -> APP_ADVERT
            22 -> APP_AUDIO
            23 -> APP_BUTTON
            24 -> APP_CARD
            25 -> APP_CROSSWORDS
            26 -> APP_ENGAGEMENT_BANNER
            27 -> APP_EPIC
            28 -> APP_GALLERY
            29 -> APP_LINK
            30 -> APP_NAVIGATION_ITEM
            31 -> APP_SCREEN
            32 -> APP_THRASHER
            33 -> APP_VIDEO
            34 -> AUDIO_ATOM
            35 -> CHART_ATOM
            36 -> ACQUISITIONS_MERCHANDISING
            37 -> ACQUISITIONS_HOUSE_ADS
            38 -> SIGN_IN_GATE
            39 -> ACQUISITIONS_SUBSCRIPTIONS_BANNER
            40 -> MOBILE_STICKY_AD
            41 -> IDENTITY_AUTHENTICATION
            42 -> RETENTION_ENGAGEMENT_BANNER
            43 -> ACQUISITION_SUPPORT_SITE
            44 -> RETENTION_EPIC
            else -> null
        }
    }
}

/**
 * Struct name appended with V2 as otherwise the classes generated by update_avro_schema.sh
 * in ophan-data-lake would not compile since the Component would be permanently hidden
 * by the ophan.thrift.event.Component import.
 */
data class ComponentV2(
    @JvmField @ThriftField(fieldId = 1, isRequired = true) val componentType: ComponentType,
    @JvmField @ThriftField(fieldId = 2, isOptional = true) val id: String?,
    @JvmField @ThriftField(fieldId = 3, isRequired = true) val products: Set<Product>,
    @JvmField @ThriftField(fieldId = 4, isOptional = true) val campaignCode: String?,
    @JvmField @ThriftField(fieldId = 5, isRequired = true) val labels: Set<String>
) : Struct {
    override fun write(protocol: Protocol) {
        ADAPTER.write(protocol, this)
    }

    class Builder : StructBuilder<ComponentV2> {
        private var componentType: ComponentType? = null

        private var id: String? = null

        private var products: Set<Product>? = null

        private var campaignCode: String? = null

        private var labels: Set<String>? = null

        constructor() {
            this.componentType = null
            this.id = null
            this.products = null
            this.campaignCode = null
            this.labels = null
        }

        constructor(source: ComponentV2) {
            this.componentType = source.componentType
            this.id = source.id
            this.products = source.products
            this.campaignCode = source.campaignCode
            this.labels = source.labels
        }

        fun componentType(componentType: ComponentType) = apply { this.componentType = componentType }

        fun id(id: String?) = apply { this.id = id }

        fun products(products: Set<Product>) = apply { this.products = products }

        fun campaignCode(campaignCode: String?) = apply { this.campaignCode = campaignCode }

        fun labels(labels: Set<String>) = apply { this.labels = labels }

        override fun build(): ComponentV2 = ComponentV2(componentType = checkNotNull(componentType) { "Required field 'componentType' is missing" },
                id = this.id,
                products = checkNotNull(products) { "Required field 'products' is missing" },
                campaignCode = this.campaignCode,
                labels = checkNotNull(labels) { "Required field 'labels' is missing" })
        override fun reset() {
            this.componentType = null
            this.id = null
            this.products = null
            this.campaignCode = null
            this.labels = null
        }
    }

    private class ComponentV2Adapter : Adapter<ComponentV2, Builder> {
        override fun read(protocol: Protocol) = read(protocol, Builder())

        override fun read(protocol: Protocol, builder: Builder): ComponentV2 {
            protocol.readStructBegin()
            while (true) {
                val fieldMeta = protocol.readFieldBegin()
                if (fieldMeta.typeId == TType.STOP) {
                    break
                }
                when (fieldMeta.fieldId.toInt()) {
                    1 -> {
                        if (fieldMeta.typeId == TType.I32) {
                            val componentType = protocol.readI32().let {
                                ComponentType.findByValue(it) ?: throw ThriftException(ThriftException.Kind.PROTOCOL_ERROR, "Unexpected value for enum type ComponentType: $it")
                            }
                            builder.componentType(componentType)
                        } else {
                            ProtocolUtil.skip(protocol, fieldMeta.typeId)
                        }
                    }
                    2 -> {
                        if (fieldMeta.typeId == TType.STRING) {
                            val id = protocol.readString()
                            builder.id(id)
                        } else {
                            ProtocolUtil.skip(protocol, fieldMeta.typeId)
                        }
                    }
                    3 -> {
                        if (fieldMeta.typeId == TType.SET) {
                            val set0 = protocol.readSetBegin()
                            val products = LinkedHashSet<Product>(set0.size)
                            for (i0 in 0 until set0.size) {
                                val item0 = protocol.readI32().let {
                                    Product.findByValue(it) ?: throw ThriftException(ThriftException.Kind.PROTOCOL_ERROR, "Unexpected value for enum type Product: $it")
                                }
                                products += item0
                            }
                            protocol.readSetEnd()
                            builder.products(products)
                        } else {
                            ProtocolUtil.skip(protocol, fieldMeta.typeId)
                        }
                    }
                    4 -> {
                        if (fieldMeta.typeId == TType.STRING) {
                            val campaignCode = protocol.readString()
                            builder.campaignCode(campaignCode)
                        } else {
                            ProtocolUtil.skip(protocol, fieldMeta.typeId)
                        }
                    }
                    5 -> {
                        if (fieldMeta.typeId == TType.SET) {
                            val set0 = protocol.readSetBegin()
                            val labels = LinkedHashSet<String>(set0.size)
                            for (i0 in 0 until set0.size) {
                                val item0 = protocol.readString()
                                labels += item0
                            }
                            protocol.readSetEnd()
                            builder.labels(labels)
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

        override fun write(protocol: Protocol, struct: ComponentV2) {
            protocol.writeStructBegin("ComponentV2")
            protocol.writeFieldBegin("componentType", 1, TType.I32)
            protocol.writeI32(struct.componentType.value)
            protocol.writeFieldEnd()
            if (struct.id != null) {
                protocol.writeFieldBegin("id", 2, TType.STRING)
                protocol.writeString(struct.id)
                protocol.writeFieldEnd()
            }
            protocol.writeFieldBegin("products", 3, TType.SET)
            protocol.writeSetBegin(TType.I32, struct.products.size)
            for (item0 in struct.products) {
                protocol.writeI32(item0.value)
            }
            protocol.writeSetEnd()
            protocol.writeFieldEnd()
            if (struct.campaignCode != null) {
                protocol.writeFieldBegin("campaignCode", 4, TType.STRING)
                protocol.writeString(struct.campaignCode)
                protocol.writeFieldEnd()
            }
            protocol.writeFieldBegin("labels", 5, TType.SET)
            protocol.writeSetBegin(TType.STRING, struct.labels.size)
            for (item0 in struct.labels) {
                protocol.writeString(item0)
            }
            protocol.writeSetEnd()
            protocol.writeFieldEnd()
            protocol.writeFieldStop()
            protocol.writeStructEnd()
        }
    }

    companion object {
        @JvmField
        val ADAPTER: Adapter<ComponentV2, Builder> = ComponentV2Adapter()
    }
}

/**
 * An event representing an action taken against a component on the web or apps.
 */
data class ComponentEvent(
    @JvmField @ThriftField(fieldId = 1, isRequired = true) val component: ComponentV2,
    @JvmField @ThriftField(fieldId = 2, isRequired = true) val action: Action,
    @JvmField @ThriftField(fieldId = 3, isOptional = true) val value: String?,
    @JvmField @ThriftField(fieldId = 4, isOptional = true) val id: String?,
    @JvmField @ThriftField(fieldId = 5, isOptional = true) val abTest: AbTest?
) : Struct {
    override fun write(protocol: Protocol) {
        ADAPTER.write(protocol, this)
    }

    class Builder : StructBuilder<ComponentEvent> {
        private var component: ComponentV2? = null

        private var action: Action? = null

        private var value: String? = null

        private var id: String? = null

        private var abTest: AbTest? = null

        constructor() {
            this.component = null
            this.action = null
            this.value = null
            this.id = null
            this.abTest = null
        }

        constructor(source: ComponentEvent) {
            this.component = source.component
            this.action = source.action
            this.value = source.value
            this.id = source.id
            this.abTest = source.abTest
        }

        fun component(component: ComponentV2) = apply { this.component = component }

        fun action(action: Action) = apply { this.action = action }

        fun value(value: String?) = apply { this.value = value }

        fun id(id: String?) = apply { this.id = id }

        fun abTest(abTest: AbTest?) = apply { this.abTest = abTest }

        override fun build(): ComponentEvent = ComponentEvent(component = checkNotNull(component) { "Required field 'component' is missing" },
                action = checkNotNull(action) { "Required field 'action' is missing" },
                value = this.value, id = this.id, abTest = this.abTest)
        override fun reset() {
            this.component = null
            this.action = null
            this.value = null
            this.id = null
            this.abTest = null
        }
    }

    private class ComponentEventAdapter : Adapter<ComponentEvent, Builder> {
        override fun read(protocol: Protocol) = read(protocol, Builder())

        override fun read(protocol: Protocol, builder: Builder): ComponentEvent {
            protocol.readStructBegin()
            while (true) {
                val fieldMeta = protocol.readFieldBegin()
                if (fieldMeta.typeId == TType.STOP) {
                    break
                }
                when (fieldMeta.fieldId.toInt()) {
                    1 -> {
                        if (fieldMeta.typeId == TType.STRUCT) {
                            val component = ComponentV2.ADAPTER.read(protocol)
                            builder.component(component)
                        } else {
                            ProtocolUtil.skip(protocol, fieldMeta.typeId)
                        }
                    }
                    2 -> {
                        if (fieldMeta.typeId == TType.I32) {
                            val action = protocol.readI32().let {
                                Action.findByValue(it) ?: throw ThriftException(ThriftException.Kind.PROTOCOL_ERROR, "Unexpected value for enum type Action: $it")
                            }
                            builder.action(action)
                        } else {
                            ProtocolUtil.skip(protocol, fieldMeta.typeId)
                        }
                    }
                    3 -> {
                        if (fieldMeta.typeId == TType.STRING) {
                            val value = protocol.readString()
                            builder.value(value)
                        } else {
                            ProtocolUtil.skip(protocol, fieldMeta.typeId)
                        }
                    }
                    4 -> {
                        if (fieldMeta.typeId == TType.STRING) {
                            val id = protocol.readString()
                            builder.id(id)
                        } else {
                            ProtocolUtil.skip(protocol, fieldMeta.typeId)
                        }
                    }
                    5 -> {
                        if (fieldMeta.typeId == TType.STRUCT) {
                            val abTest = AbTest.ADAPTER.read(protocol)
                            builder.abTest(abTest)
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

        override fun write(protocol: Protocol, struct: ComponentEvent) {
            protocol.writeStructBegin("ComponentEvent")
            protocol.writeFieldBegin("component", 1, TType.STRUCT)
            ComponentV2.ADAPTER.write(protocol, struct.component)
            protocol.writeFieldEnd()
            protocol.writeFieldBegin("action", 2, TType.I32)
            protocol.writeI32(struct.action.value)
            protocol.writeFieldEnd()
            if (struct.value != null) {
                protocol.writeFieldBegin("value", 3, TType.STRING)
                protocol.writeString(struct.value)
                protocol.writeFieldEnd()
            }
            if (struct.id != null) {
                protocol.writeFieldBegin("id", 4, TType.STRING)
                protocol.writeString(struct.id)
                protocol.writeFieldEnd()
            }
            if (struct.abTest != null) {
                protocol.writeFieldBegin("abTest", 5, TType.STRUCT)
                AbTest.ADAPTER.write(protocol, struct.abTest)
                protocol.writeFieldEnd()
            }
            protocol.writeFieldStop()
            protocol.writeStructEnd()
        }
    }

    companion object {
        @JvmField
        val ADAPTER: Adapter<ComponentEvent, Builder> = ComponentEventAdapter()
    }
}
