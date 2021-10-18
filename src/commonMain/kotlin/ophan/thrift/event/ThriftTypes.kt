package ophan.thrift.event

import com.microsoft.thrifty.Adapter
import com.microsoft.thrifty.Struct
import com.microsoft.thrifty.StructBuilder
import com.microsoft.thrifty.TType
import com.microsoft.thrifty.ThriftException
import com.microsoft.thrifty.ThriftField
import com.microsoft.thrifty.protocol.Protocol
import com.microsoft.thrifty.util.ProtocolUtil
import kotlin.Boolean
import kotlin.Double
import kotlin.Int
import kotlin.Long
import kotlin.Short
import kotlin.String
import kotlin.collections.ArrayList
import kotlin.collections.LinkedHashSet
import kotlin.collections.List
import kotlin.collections.Set
import kotlin.jvm.JvmField
import kotlin.jvm.JvmStatic
import ophan.thrift.componentEvent.ComponentType
import ophan.thrift.nativeapp.Source

/**
 * Indicates sites that we really care about, factoring out the variation
 * on domain that they sometimes contain.
 */
enum class SignificantSite(@JvmField val value: Int) {
    /**
     * The Guardian website, including when viewed on native apps and
     * for pages served on non www. subdomains such as membership.theguardian.com
     * and careers.theguardian.com
     */
    GUARDIAN(0),

    /**
     * An email sent by the Guardian, either one of our scheduled emails
     * or by a user clicking "email this"  *
     */
    GUARDIAN_EMAIL(1),

    /**
     * An app push alert sent by the Guardian
     */
    GUARDIAN_PUSH(15),

    /**
     * Any google website, regardless of subdomain and country domain
     * i.e. includes news.google.co.uk and www.google.ca
     * also includes google plus?
     */
    GOOGLE(2),

    /**
     * Twitter, including any where the source was a result of going via
     * the t.co redirector
     */
    TWITTER(3),

    /**
     * Facebook, including when we've inferrer that the referrer was facebook
     * because we were access inside a facebook native app.
     */
    FACEBOOK(4),

    REDDIT(5),

    DRUDGE_REPORT(6),

    /**
     * Outbrain, both paid and non-paid *
     */
    OUTBRAIN(7),

    TUMBLR(8),

    PINTEREST(9),

    DIGG(10),

    STUMBLEUPON(11),

    FLIPBOARD(12),

    LINKEDIN(13),

    BING(14),

    SPOTLIGHT(16),

    WE_CHAT(17),

    WHATS_APP(18),

    APPLE_NEWS(19),

    IN_SHORTS(20),

    UPDAY(21),

    SMART_NEWS(22),

    NEWS_BREAK(23);

    companion object {
        @JvmStatic
        fun findByValue(value: Int): SignificantSite? = when (value) {
            0 -> GUARDIAN
            1 -> GUARDIAN_EMAIL
            15 -> GUARDIAN_PUSH
            2 -> GOOGLE
            3 -> TWITTER
            4 -> FACEBOOK
            5 -> REDDIT
            6 -> DRUDGE_REPORT
            7 -> OUTBRAIN
            8 -> TUMBLR
            9 -> PINTEREST
            10 -> DIGG
            11 -> STUMBLEUPON
            12 -> FLIPBOARD
            13 -> LINKEDIN
            14 -> BING
            16 -> SPOTLIGHT
            17 -> WE_CHAT
            18 -> WHATS_APP
            19 -> APPLE_NEWS
            20 -> IN_SHORTS
            21 -> UPDAY
            22 -> SMART_NEWS
            23 -> NEWS_BREAK
            else -> null
        }
    }
}

/**
 * The platform that served this request to the reader.
 */
enum class Platform(@JvmField val value: Int) {
    R2(0),

    NEXT_GEN(1),

    IOS_NATIVE_APP(2),

    ANDROID_NATIVE_APP(3),

    /**
     * Served as a result of embedding a media item on a third party site.
     * Note therefore you should not typically include this as a guardian
     * "page view". *
     */
    EMBED(4),

    MEMBERSHIP(5),

    FACEBOOK_INSTANT_ARTICLE(6),

    /**
     * https://www.ampproject.org/
     */
    AMP(7),

    WITNESS(8),

    JOBS(9),

    CONTRIBUTION(10),

    YAHOO(11),

    AMAZON_ECHO(12),

    APPLE_NEWS(13),

    WINDOWS_NATIVE_APP(14),

    SCRIBD(15),

    SUPPORT(16),

    SUBSCRIBE(17),

    MANAGE_MY_ACCOUNT(18),

    SMART_NEWS(19),

    /**
     * also called "The Daily Edition" and other names
     */
    EDITIONS(20);

    companion object {
        @JvmStatic
        fun findByValue(value: Int): Platform? = when (value) {
            0 -> R2
            1 -> NEXT_GEN
            2 -> IOS_NATIVE_APP
            3 -> ANDROID_NATIVE_APP
            4 -> EMBED
            5 -> MEMBERSHIP
            6 -> FACEBOOK_INSTANT_ARTICLE
            7 -> AMP
            8 -> WITNESS
            9 -> JOBS
            10 -> CONTRIBUTION
            11 -> YAHOO
            12 -> AMAZON_ECHO
            13 -> APPLE_NEWS
            14 -> WINDOWS_NATIVE_APP
            15 -> SCRIBD
            16 -> SUPPORT
            17 -> SUBSCRIBE
            18 -> MANAGE_MY_ACCOUNT
            19 -> SMART_NEWS
            20 -> EDITIONS
            else -> null
        }
    }
}

enum class MediaType(@JvmField val value: Int) {
    VIDEO(1),

    AUDIO(2);

    companion object {
        @JvmStatic
        fun findByValue(value: Int): MediaType? = when (value) {
            1 -> VIDEO
            2 -> AUDIO
            else -> null
        }
    }
}

enum class MediaEvent(@JvmField val value: Int) {
    /**
     * The media has been requested.
     * Currently, it appears this event is only received for pre-roll videos.
     */
    REQUEST(1),

    /**
     * The media is ready to play.
     */
    READY(2),

    /**
     * The media has started playing.
     */
    PLAY(3),

    /**
     * The media has played a quarter of the way through.
     * Currently, it appears that pre-roll videos do not send this event.
     */
    PERCENT25(4),

    /**
     * The media has played half way though.
     * Currently, it appears that pre-roll videos do not send this event.
     */
    PERCENT50(5),

    /**
     * The media has played three quarters of the way though.
     * Currently, it appears that pre-roll videos do not send this event.
     */
    PERCENT75(6),

    /**
     * The media has played to the end.
     * NB: "END" is a reserved word in thrift apparently
     */
    THE_END(7);

    companion object {
        @JvmStatic
        fun findByValue(value: Int): MediaEvent? = when (value) {
            1 -> REQUEST
            2 -> READY
            3 -> PLAY
            4 -> PERCENT25
            5 -> PERCENT50
            6 -> PERCENT75
            7 -> THE_END
            else -> null
        }
    }
}

enum class Product(@JvmField val value: Int) {
    /**
     * A one-off contribution
     */
    CONTRIBUTION(1),

    /**
     * A recurring contribution
     */
    RECURRING_CONTRIBUTION(2),

    /**
     * A Membership Supporter signup
     */
    MEMBERSHIP_SUPPORTER(3),

    /**
     * A Membership Patron signup
     */
    MEMBERSHIP_PATRON(4),

    /**
     * A Membership Partner signup
     */
    MEMBERSHIP_PARTNER(5),

    /**
     * A digital subscription
     */
    DIGITAL_SUBSCRIPTION(6),

    /**
     * DEPRECATED DO NOT USE
     */
    PAPER_SUBSCRIPTION_EVERYDAY(7),

    /**
     * DEPRECATED DO NOT USE
     */
    PAPER_SUBSCRIPTION_SIXDAY(8),

    /**
     * DEPRECATED DO NOT USE
     */
    PAPER_SUBSCRIPTION_WEEKEND(9),

    /**
     * DEPRECATED DO NOT USE
     */
    PAPER_SUBSCRIPTION_SUNDAY(10),

    /**
     * A paper subscription
     */
    PRINT_SUBSCRIPTION(11),

    /**
     * An app premium tier signup
     */
    APP_PREMIUM_TIER(12);

    companion object {
        @JvmStatic
        fun findByValue(value: Int): Product? = when (value) {
            1 -> CONTRIBUTION
            2 -> RECURRING_CONTRIBUTION
            3 -> MEMBERSHIP_SUPPORTER
            4 -> MEMBERSHIP_PATRON
            5 -> MEMBERSHIP_PARTNER
            6 -> DIGITAL_SUBSCRIPTION
            7 -> PAPER_SUBSCRIPTION_EVERYDAY
            8 -> PAPER_SUBSCRIPTION_SIXDAY
            9 -> PAPER_SUBSCRIPTION_WEEKEND
            10 -> PAPER_SUBSCRIPTION_SUNDAY
            11 -> PRINT_SUBSCRIPTION
            12 -> APP_PREMIUM_TIER
            else -> null
        }
    }
}

enum class PaymentFrequency(@JvmField val value: Int) {
    ONE_OFF(1),

    MONTHLY(2),

    ANNUALLY(3),

    QUARTERLY(4),

    SIX_MONTHLY(5);

    companion object {
        @JvmStatic
        fun findByValue(value: Int): PaymentFrequency? = when (value) {
            1 -> ONE_OFF
            2 -> MONTHLY
            3 -> ANNUALLY
            4 -> QUARTERLY
            5 -> SIX_MONTHLY
            else -> null
        }
    }
}

enum class PaymentProvider(@JvmField val value: Int) {
    STRIPE(1),

    PAYPAL(2),

    GOCARDLESS(3),

    IN_APP_PURCHASE(4),

    STRIPE_APPLE_PAY(5),

    STRIPE_PAYMENT_REQUEST_BUTTON(6),

    SUBSCRIBE_WITH_GOOGLE(7),

    AMAZON_PAY(8),

    STRIPE_SEPA(9);

    companion object {
        @JvmStatic
        fun findByValue(value: Int): PaymentProvider? = when (value) {
            1 -> STRIPE
            2 -> PAYPAL
            3 -> GOCARDLESS
            4 -> IN_APP_PURCHASE
            5 -> STRIPE_APPLE_PAY
            6 -> STRIPE_PAYMENT_REQUEST_BUTTON
            7 -> SUBSCRIBE_WITH_GOOGLE
            8 -> AMAZON_PAY
            9 -> STRIPE_SEPA
            else -> null
        }
    }
}

enum class AcquisitionSource(@JvmField val value: Int) {
    GUARDIAN_WEB(1),

    /**
     * DEPRECATED DO NOT USE
     */
    GUARDIAN_APPS(2),

    EMAIL(3),

    SOCIAL(4),

    SEARCH(5),

    PPC(6),

    DIRECT(7),

    GUARDIAN_APP_IOS(8),

    GUARDIAN_APP_ANDROID(9),

    APPLE_NEWS(10),

    GOOGLE_AMP(11),

    YOUTUBE(12);

    companion object {
        @JvmStatic
        fun findByValue(value: Int): AcquisitionSource? = when (value) {
            1 -> GUARDIAN_WEB
            2 -> GUARDIAN_APPS
            3 -> EMAIL
            4 -> SOCIAL
            5 -> SEARCH
            6 -> PPC
            7 -> DIRECT
            8 -> GUARDIAN_APP_IOS
            9 -> GUARDIAN_APP_ANDROID
            10 -> APPLE_NEWS
            11 -> GOOGLE_AMP
            12 -> YOUTUBE
            else -> null
        }
    }
}

/**
 * Differentiates between the (many) different types of print product
 */
enum class PrintProduct(@JvmField val value: Int) {
    VOUCHER_SATURDAY(1),

    VOUCHER_SATURDAY_PLUS(2),

    VOUCHER_WEEKLY_AND_SATURDAY(3),

    VOUCHER_WEEKLY_AND_SATURDAY_PLUS(4),

    VOUCHER_SUNDAY(5),

    VOUCHER_SUNDAY_PLUS(6),

    /**
     * DEPRECATED DO NOT USE
     * This enum has a spelling mistake. Use VOUCHER_WEEKEND (27) instead
     */
    VOUCER_WEEKEND(7),

    /**
     * DEPRECATED DO NOT USE
     * This enum has a spelling mistake. Use VOUCHER_WEEKEND_PLUS (28) instead
     */
    VOUCER_WEEKEND_PLUS(8),

    VOUCHER_SIXDAY(9),

    VOUCHER_SIXDAY_PLUS(10),

    VOUCHER_EVERYDAY(11),

    VOUCHER_EVERYDAY_PLUS(12),

    HOME_DELIVERY_SATURDAY(13),

    HOME_DELIVERY_SATURDAY_PLUS(14),

    HOME_DELIVERY_WEEKLY_AND_SATURDAY(15),

    HOME_DELIVERY_WEEKLY_AND_SATURDAY_PLUS(16),

    HOME_DELIVERY_SUNDAY(17),

    HOME_DELIVERY_SUNDAY_PLUS(18),

    HOME_DELIVERY_WEEKEND(19),

    HOME_DELIVERY_WEEKEND_PLUS(20),

    HOME_DELIVERY_SIXDAY(21),

    HOME_DELIVERY_SIXDAY_PLUS(22),

    HOME_DELIVERY_EVERYDAY(23),

    HOME_DELIVERY_EVERYDAY_PLUS(24),

    GUARDIAN_WEEKLY(25),

    GUARDIAN_WEEKLY_PLUS(26),

    VOUCHER_WEEKEND(27),

    VOUCHER_WEEKEND_PLUS(28);

    companion object {
        @JvmStatic
        fun findByValue(value: Int): PrintProduct? = when (value) {
            1 -> VOUCHER_SATURDAY
            2 -> VOUCHER_SATURDAY_PLUS
            3 -> VOUCHER_WEEKLY_AND_SATURDAY
            4 -> VOUCHER_WEEKLY_AND_SATURDAY_PLUS
            5 -> VOUCHER_SUNDAY
            6 -> VOUCHER_SUNDAY_PLUS
            7 -> VOUCER_WEEKEND
            8 -> VOUCER_WEEKEND_PLUS
            9 -> VOUCHER_SIXDAY
            10 -> VOUCHER_SIXDAY_PLUS
            11 -> VOUCHER_EVERYDAY
            12 -> VOUCHER_EVERYDAY_PLUS
            13 -> HOME_DELIVERY_SATURDAY
            14 -> HOME_DELIVERY_SATURDAY_PLUS
            15 -> HOME_DELIVERY_WEEKLY_AND_SATURDAY
            16 -> HOME_DELIVERY_WEEKLY_AND_SATURDAY_PLUS
            17 -> HOME_DELIVERY_SUNDAY
            18 -> HOME_DELIVERY_SUNDAY_PLUS
            19 -> HOME_DELIVERY_WEEKEND
            20 -> HOME_DELIVERY_WEEKEND_PLUS
            21 -> HOME_DELIVERY_SIXDAY
            22 -> HOME_DELIVERY_SIXDAY_PLUS
            23 -> HOME_DELIVERY_EVERYDAY
            24 -> HOME_DELIVERY_EVERYDAY_PLUS
            25 -> GUARDIAN_WEEKLY
            26 -> GUARDIAN_WEEKLY_PLUS
            27 -> VOUCHER_WEEKEND
            28 -> VOUCHER_WEEKEND_PLUS
            else -> null
        }
    }
}

/**
 * Represents the rendering of an ad on this page.
 */
data class RenderedAd(
    @JvmField @ThriftField(fieldId = 1, isRequired = true) val slot: String,
    @JvmField @ThriftField(fieldId = 2, isOptional = true) val lineItemId: Long?,
    @JvmField @ThriftField(fieldId = 3, isOptional = true) val creativeId: Long?,
    @JvmField @ThriftField(fieldId = 4, isOptional = true) val timeToRenderEndedMs: Long?,
    @JvmField @ThriftField(fieldId = 5, isOptional = true) val timeToAdRequestMs: Long?,
    @JvmField @ThriftField(fieldId = 6, isOptional = true) val adRetrievalTimeMs: Long?,
    @JvmField @ThriftField(fieldId = 7, isOptional = true) val adRenderTimeMs: Long?
) : Struct {
    override fun write(protocol: Protocol) {
        ADAPTER.write(protocol, this)
    }

    class Builder : StructBuilder<RenderedAd> {
        private var slot: String? = null

        private var lineItemId: Long? = null

        private var creativeId: Long? = null

        private var timeToRenderEndedMs: Long? = null

        private var timeToAdRequestMs: Long? = null

        private var adRetrievalTimeMs: Long? = null

        private var adRenderTimeMs: Long? = null

        constructor() {
            this.slot = null
            this.lineItemId = null
            this.creativeId = null
            this.timeToRenderEndedMs = null
            this.timeToAdRequestMs = null
            this.adRetrievalTimeMs = null
            this.adRenderTimeMs = null
        }

        constructor(source: RenderedAd) {
            this.slot = source.slot
            this.lineItemId = source.lineItemId
            this.creativeId = source.creativeId
            this.timeToRenderEndedMs = source.timeToRenderEndedMs
            this.timeToAdRequestMs = source.timeToAdRequestMs
            this.adRetrievalTimeMs = source.adRetrievalTimeMs
            this.adRenderTimeMs = source.adRenderTimeMs
        }

        fun slot(slot: String) = apply { this.slot = slot }

        fun lineItemId(lineItemId: Long?) = apply { this.lineItemId = lineItemId }

        fun creativeId(creativeId: Long?) = apply { this.creativeId = creativeId }

        fun timeToRenderEndedMs(timeToRenderEndedMs: Long?) = apply { this.timeToRenderEndedMs = timeToRenderEndedMs }

        fun timeToAdRequestMs(timeToAdRequestMs: Long?) = apply { this.timeToAdRequestMs = timeToAdRequestMs }

        fun adRetrievalTimeMs(adRetrievalTimeMs: Long?) = apply { this.adRetrievalTimeMs = adRetrievalTimeMs }

        fun adRenderTimeMs(adRenderTimeMs: Long?) = apply { this.adRenderTimeMs = adRenderTimeMs }

        override fun build(): RenderedAd = RenderedAd(slot = checkNotNull(slot) { "Required field 'slot' is missing" },
                lineItemId = this.lineItemId, creativeId = this.creativeId,
                timeToRenderEndedMs = this.timeToRenderEndedMs,
                timeToAdRequestMs = this.timeToAdRequestMs,
                adRetrievalTimeMs = this.adRetrievalTimeMs, adRenderTimeMs = this.adRenderTimeMs)
        override fun reset() {
            this.slot = null
            this.lineItemId = null
            this.creativeId = null
            this.timeToRenderEndedMs = null
            this.timeToAdRequestMs = null
            this.adRetrievalTimeMs = null
            this.adRenderTimeMs = null
        }
    }

    private class RenderedAdAdapter : Adapter<RenderedAd, Builder> {
        override fun read(protocol: Protocol) = read(protocol, Builder())

        override fun read(protocol: Protocol, builder: Builder): RenderedAd {
            protocol.readStructBegin()
            while (true) {
                val fieldMeta = protocol.readFieldBegin()
                if (fieldMeta.typeId == TType.STOP) {
                    break
                }
                when (fieldMeta.fieldId.toInt()) {
                    1 -> {
                        if (fieldMeta.typeId == TType.STRING) {
                            val slot = protocol.readString()
                            builder.slot(slot)
                        } else {
                            ProtocolUtil.skip(protocol, fieldMeta.typeId)
                        }
                    }
                    2 -> {
                        if (fieldMeta.typeId == TType.I64) {
                            val lineItemId = protocol.readI64()
                            builder.lineItemId(lineItemId)
                        } else {
                            ProtocolUtil.skip(protocol, fieldMeta.typeId)
                        }
                    }
                    3 -> {
                        if (fieldMeta.typeId == TType.I64) {
                            val creativeId = protocol.readI64()
                            builder.creativeId(creativeId)
                        } else {
                            ProtocolUtil.skip(protocol, fieldMeta.typeId)
                        }
                    }
                    4 -> {
                        if (fieldMeta.typeId == TType.I64) {
                            val timeToRenderEndedMs = protocol.readI64()
                            builder.timeToRenderEndedMs(timeToRenderEndedMs)
                        } else {
                            ProtocolUtil.skip(protocol, fieldMeta.typeId)
                        }
                    }
                    5 -> {
                        if (fieldMeta.typeId == TType.I64) {
                            val timeToAdRequestMs = protocol.readI64()
                            builder.timeToAdRequestMs(timeToAdRequestMs)
                        } else {
                            ProtocolUtil.skip(protocol, fieldMeta.typeId)
                        }
                    }
                    6 -> {
                        if (fieldMeta.typeId == TType.I64) {
                            val adRetrievalTimeMs = protocol.readI64()
                            builder.adRetrievalTimeMs(adRetrievalTimeMs)
                        } else {
                            ProtocolUtil.skip(protocol, fieldMeta.typeId)
                        }
                    }
                    7 -> {
                        if (fieldMeta.typeId == TType.I64) {
                            val adRenderTimeMs = protocol.readI64()
                            builder.adRenderTimeMs(adRenderTimeMs)
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

        override fun write(protocol: Protocol, struct: RenderedAd) {
            protocol.writeStructBegin("RenderedAd")
            protocol.writeFieldBegin("slot", 1, TType.STRING)
            protocol.writeString(struct.slot)
            protocol.writeFieldEnd()
            if (struct.lineItemId != null) {
                protocol.writeFieldBegin("lineItemId", 2, TType.I64)
                protocol.writeI64(struct.lineItemId)
                protocol.writeFieldEnd()
            }
            if (struct.creativeId != null) {
                protocol.writeFieldBegin("creativeId", 3, TType.I64)
                protocol.writeI64(struct.creativeId)
                protocol.writeFieldEnd()
            }
            if (struct.timeToRenderEndedMs != null) {
                protocol.writeFieldBegin("timeToRenderEndedMs", 4, TType.I64)
                protocol.writeI64(struct.timeToRenderEndedMs)
                protocol.writeFieldEnd()
            }
            if (struct.timeToAdRequestMs != null) {
                protocol.writeFieldBegin("timeToAdRequestMs", 5, TType.I64)
                protocol.writeI64(struct.timeToAdRequestMs)
                protocol.writeFieldEnd()
            }
            if (struct.adRetrievalTimeMs != null) {
                protocol.writeFieldBegin("adRetrievalTimeMs", 6, TType.I64)
                protocol.writeI64(struct.adRetrievalTimeMs)
                protocol.writeFieldEnd()
            }
            if (struct.adRenderTimeMs != null) {
                protocol.writeFieldBegin("adRenderTimeMs", 7, TType.I64)
                protocol.writeI64(struct.adRenderTimeMs)
                protocol.writeFieldEnd()
            }
            protocol.writeFieldStop()
            protocol.writeStructEnd()
        }
    }

    companion object {
        @JvmField
        val ADAPTER: Adapter<RenderedAd, Builder> = RenderedAdAdapter()
    }
}

/**
 * Represents the AbTest on the web and apps
 */
data class AbTest(
    @JvmField @ThriftField(fieldId = 1, isRequired = true) val name: String,
    @JvmField @ThriftField(fieldId = 2, isRequired = true) val variant: String,
    @JvmField @ThriftField(fieldId = 3, isOptional = true) val complete: Boolean?,
    @JvmField @ThriftField(fieldId = 4, isOptional = true) val campaignCodes: Set<String>?
) : Struct {
    override fun write(protocol: Protocol) {
        ADAPTER.write(protocol, this)
    }

    class Builder : StructBuilder<AbTest> {
        private var name: String? = null

        private var variant: String? = null

        private var complete: Boolean? = null

        private var campaignCodes: Set<String>? = null

        constructor() {
            this.name = null
            this.variant = null
            this.complete = null
            this.campaignCodes = null
        }

        constructor(source: AbTest) {
            this.name = source.name
            this.variant = source.variant
            this.complete = source.complete
            this.campaignCodes = source.campaignCodes
        }

        fun name(name: String) = apply { this.name = name }

        fun variant(variant: String) = apply { this.variant = variant }

        fun complete(complete: Boolean?) = apply { this.complete = complete }

        fun campaignCodes(campaignCodes: Set<String>?) = apply { this.campaignCodes = campaignCodes }

        override fun build(): AbTest = AbTest(name = checkNotNull(name) { "Required field 'name' is missing" },
                variant = checkNotNull(variant) { "Required field 'variant' is missing" },
                complete = this.complete, campaignCodes = this.campaignCodes)
        override fun reset() {
            this.name = null
            this.variant = null
            this.complete = null
            this.campaignCodes = null
        }
    }

    private class AbTestAdapter : Adapter<AbTest, Builder> {
        override fun read(protocol: Protocol) = read(protocol, Builder())

        override fun read(protocol: Protocol, builder: Builder): AbTest {
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
                            val variant = protocol.readString()
                            builder.variant(variant)
                        } else {
                            ProtocolUtil.skip(protocol, fieldMeta.typeId)
                        }
                    }
                    3 -> {
                        if (fieldMeta.typeId == TType.BOOL) {
                            val complete = protocol.readBool()
                            builder.complete(complete)
                        } else {
                            ProtocolUtil.skip(protocol, fieldMeta.typeId)
                        }
                    }
                    4 -> {
                        if (fieldMeta.typeId == TType.SET) {
                            val set0 = protocol.readSetBegin()
                            val campaignCodes = LinkedHashSet<String>(set0.size)
                            for (i0 in 0 until set0.size) {
                                val item0 = protocol.readString()
                                campaignCodes += item0
                            }
                            protocol.readSetEnd()
                            builder.campaignCodes(campaignCodes)
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

        override fun write(protocol: Protocol, struct: AbTest) {
            protocol.writeStructBegin("AbTest")
            protocol.writeFieldBegin("name", 1, TType.STRING)
            protocol.writeString(struct.name)
            protocol.writeFieldEnd()
            protocol.writeFieldBegin("variant", 2, TType.STRING)
            protocol.writeString(struct.variant)
            protocol.writeFieldEnd()
            if (struct.complete != null) {
                protocol.writeFieldBegin("complete", 3, TType.BOOL)
                protocol.writeBool(struct.complete)
                protocol.writeFieldEnd()
            }
            if (struct.campaignCodes != null) {
                protocol.writeFieldBegin("campaignCodes", 4, TType.SET)
                protocol.writeSetBegin(TType.STRING, struct.campaignCodes.size)
                for (item0 in struct.campaignCodes) {
                    protocol.writeString(item0)
                }
                protocol.writeSetEnd()
                protocol.writeFieldEnd()
            }
            protocol.writeFieldStop()
            protocol.writeStructEnd()
        }
    }

    companion object {
        @JvmField
        val ADAPTER: Adapter<AbTest, Builder> = AbTestAdapter()
    }
}

data class AbTestInfo(@JvmField @ThriftField(fieldId = 1, isRequired = true) val tests: Set<AbTest>) : Struct {
    override fun write(protocol: Protocol) {
        ADAPTER.write(protocol, this)
    }

    class Builder : StructBuilder<AbTestInfo> {
        private var tests: Set<AbTest>? = null

        constructor() {
            this.tests = null
        }

        constructor(source: AbTestInfo) {
            this.tests = source.tests
        }

        fun tests(tests: Set<AbTest>) = apply { this.tests = tests }

        override fun build(): AbTestInfo = AbTestInfo(tests = checkNotNull(tests) { "Required field 'tests' is missing" })
        override fun reset() {
            this.tests = null
        }
    }

    private class AbTestInfoAdapter : Adapter<AbTestInfo, Builder> {
        override fun read(protocol: Protocol) = read(protocol, Builder())

        override fun read(protocol: Protocol, builder: Builder): AbTestInfo {
            protocol.readStructBegin()
            while (true) {
                val fieldMeta = protocol.readFieldBegin()
                if (fieldMeta.typeId == TType.STOP) {
                    break
                }
                when (fieldMeta.fieldId.toInt()) {
                    1 -> {
                        if (fieldMeta.typeId == TType.SET) {
                            val set0 = protocol.readSetBegin()
                            val tests = LinkedHashSet<AbTest>(set0.size)
                            for (i0 in 0 until set0.size) {
                                val item0 = AbTest.ADAPTER.read(protocol)
                                tests += item0
                            }
                            protocol.readSetEnd()
                            builder.tests(tests)
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

        override fun write(protocol: Protocol, struct: AbTestInfo) {
            protocol.writeStructBegin("AbTestInfo")
            protocol.writeFieldBegin("tests", 1, TType.SET)
            protocol.writeSetBegin(TType.STRUCT, struct.tests.size)
            for (item0 in struct.tests) {
                AbTest.ADAPTER.write(protocol, item0)
            }
            protocol.writeSetEnd()
            protocol.writeFieldEnd()
            protocol.writeFieldStop()
            protocol.writeStructEnd()
        }
    }

    companion object {
        @JvmField
        val ADAPTER: Adapter<AbTestInfo, Builder> = AbTestInfoAdapter()
    }
}

/**
 * Represents an interaction on the web or apps
 */
data class Interaction(
    @JvmField @ThriftField(fieldId = 1, isRequired = true) val component: String,
    @JvmField @ThriftField(fieldId = 2, isOptional = true) val value: String?,
    @JvmField @ThriftField(fieldId = 3, isOptional = true) val atomId: String?
) : Struct {
    override fun write(protocol: Protocol) {
        ADAPTER.write(protocol, this)
    }

    class Builder : StructBuilder<Interaction> {
        private var component: String? = null

        private var value: String? = null

        private var atomId: String? = null

        constructor() {
            this.component = null
            this.value = null
            this.atomId = null
        }

        constructor(source: Interaction) {
            this.component = source.component
            this.value = source.value
            this.atomId = source.atomId
        }

        fun component(component: String) = apply { this.component = component }

        fun value(value: String?) = apply { this.value = value }

        fun atomId(atomId: String?) = apply { this.atomId = atomId }

        override fun build(): Interaction = Interaction(component = checkNotNull(component) { "Required field 'component' is missing" },
                value = this.value, atomId = this.atomId)
        override fun reset() {
            this.component = null
            this.value = null
            this.atomId = null
        }
    }

    private class InteractionAdapter : Adapter<Interaction, Builder> {
        override fun read(protocol: Protocol) = read(protocol, Builder())

        override fun read(protocol: Protocol, builder: Builder): Interaction {
            protocol.readStructBegin()
            while (true) {
                val fieldMeta = protocol.readFieldBegin()
                if (fieldMeta.typeId == TType.STOP) {
                    break
                }
                when (fieldMeta.fieldId.toInt()) {
                    1 -> {
                        if (fieldMeta.typeId == TType.STRING) {
                            val component = protocol.readString()
                            builder.component(component)
                        } else {
                            ProtocolUtil.skip(protocol, fieldMeta.typeId)
                        }
                    }
                    2 -> {
                        if (fieldMeta.typeId == TType.STRING) {
                            val value = protocol.readString()
                            builder.value(value)
                        } else {
                            ProtocolUtil.skip(protocol, fieldMeta.typeId)
                        }
                    }
                    3 -> {
                        if (fieldMeta.typeId == TType.STRING) {
                            val atomId = protocol.readString()
                            builder.atomId(atomId)
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

        override fun write(protocol: Protocol, struct: Interaction) {
            protocol.writeStructBegin("Interaction")
            protocol.writeFieldBegin("component", 1, TType.STRING)
            protocol.writeString(struct.component)
            protocol.writeFieldEnd()
            if (struct.value != null) {
                protocol.writeFieldBegin("value", 2, TType.STRING)
                protocol.writeString(struct.value)
                protocol.writeFieldEnd()
            }
            if (struct.atomId != null) {
                protocol.writeFieldBegin("atomId", 3, TType.STRING)
                protocol.writeString(struct.atomId)
                protocol.writeFieldEnd()
            }
            protocol.writeFieldStop()
            protocol.writeStructEnd()
        }
    }

    companion object {
        @JvmField
        val ADAPTER: Adapter<Interaction, Builder> = InteractionAdapter()
    }
}

data class AppReferral(@JvmField @ThriftField(fieldId = 1, isRequired = true) val raw: String, @JvmField @ThriftField(fieldId = 2, isOptional = true) val appId: String?) : Struct {
    override fun write(protocol: Protocol) {
        ADAPTER.write(protocol, this)
    }

    class Builder : StructBuilder<AppReferral> {
        private var raw: String? = null

        private var appId: String? = null

        constructor() {
            this.raw = null
            this.appId = null
        }

        constructor(source: AppReferral) {
            this.raw = source.raw
            this.appId = source.appId
        }

        fun raw(raw: String) = apply { this.raw = raw }

        fun appId(appId: String?) = apply { this.appId = appId }

        override fun build(): AppReferral = AppReferral(raw = checkNotNull(raw) { "Required field 'raw' is missing" },
                appId = this.appId)
        override fun reset() {
            this.raw = null
            this.appId = null
        }
    }

    private class AppReferralAdapter : Adapter<AppReferral, Builder> {
        override fun read(protocol: Protocol) = read(protocol, Builder())

        override fun read(protocol: Protocol, builder: Builder): AppReferral {
            protocol.readStructBegin()
            while (true) {
                val fieldMeta = protocol.readFieldBegin()
                if (fieldMeta.typeId == TType.STOP) {
                    break
                }
                when (fieldMeta.fieldId.toInt()) {
                    1 -> {
                        if (fieldMeta.typeId == TType.STRING) {
                            val raw = protocol.readString()
                            builder.raw(raw)
                        } else {
                            ProtocolUtil.skip(protocol, fieldMeta.typeId)
                        }
                    }
                    2 -> {
                        if (fieldMeta.typeId == TType.STRING) {
                            val appId = protocol.readString()
                            builder.appId(appId)
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

        override fun write(protocol: Protocol, struct: AppReferral) {
            protocol.writeStructBegin("AppReferral")
            protocol.writeFieldBegin("raw", 1, TType.STRING)
            protocol.writeString(struct.raw)
            protocol.writeFieldEnd()
            if (struct.appId != null) {
                protocol.writeFieldBegin("appId", 2, TType.STRING)
                protocol.writeString(struct.appId)
                protocol.writeFieldEnd()
            }
            protocol.writeFieldStop()
            protocol.writeStructEnd()
        }
    }

    companion object {
        @JvmField
        val ADAPTER: Adapter<AppReferral, Builder> = AppReferralAdapter()
    }
}

/**
 * Information about the referrer - previous page - that the reader navigated to
 * this one from.
 */
data class Referrer(
    @JvmField @ThriftField(fieldId = 1, isOptional = true) val url: Url?,
    @JvmField @ThriftField(fieldId = 4, isOptional = true) val component: String?,
    @JvmField @ThriftField(fieldId = 10, isOptional = true) val linkName: LinkName?,
    @JvmField @ThriftField(fieldId = 5, isOptional = true) val platform: Platform?,
    @JvmField @ThriftField(fieldId = 6, isOptional = true) val viewId: String?,
    @JvmField @ThriftField(fieldId = 7, isOptional = true) val email: String?,
    @JvmField @ThriftField(fieldId = 8, isOptional = true) val nativeAppSource: Source?,
    @JvmField @ThriftField(fieldId = 9, isOptional = true) val google: GoogleReferral?,
    @JvmField @ThriftField(fieldId = 11, isOptional = true) val tagIdFollowed: String?,
    @JvmField @ThriftField(fieldId = 12, isOptional = true) val appReferral: AppReferral?
) : Struct {
    override fun write(protocol: Protocol) {
        ADAPTER.write(protocol, this)
    }

    class Builder : StructBuilder<Referrer> {
        private var url: Url? = null

        private var component: String? = null

        private var linkName: LinkName? = null

        private var platform: Platform? = null

        private var viewId: String? = null

        private var email: String? = null

        private var nativeAppSource: Source? = null

        private var google: GoogleReferral? = null

        private var tagIdFollowed: String? = null

        private var appReferral: AppReferral? = null

        constructor() {
            this.url = null
            this.component = null
            this.linkName = null
            this.platform = null
            this.viewId = null
            this.email = null
            this.nativeAppSource = null
            this.google = null
            this.tagIdFollowed = null
            this.appReferral = null
        }

        constructor(source: Referrer) {
            this.url = source.url
            this.component = source.component
            this.linkName = source.linkName
            this.platform = source.platform
            this.viewId = source.viewId
            this.email = source.email
            this.nativeAppSource = source.nativeAppSource
            this.google = source.google
            this.tagIdFollowed = source.tagIdFollowed
            this.appReferral = source.appReferral
        }

        fun url(url: Url?) = apply { this.url = url }

        fun component(component: String?) = apply { this.component = component }

        fun linkName(linkName: LinkName?) = apply { this.linkName = linkName }

        fun platform(platform: Platform?) = apply { this.platform = platform }

        fun viewId(viewId: String?) = apply { this.viewId = viewId }

        fun email(email: String?) = apply { this.email = email }

        fun nativeAppSource(nativeAppSource: Source?) = apply { this.nativeAppSource = nativeAppSource }

        fun google(google: GoogleReferral?) = apply { this.google = google }

        fun tagIdFollowed(tagIdFollowed: String?) = apply { this.tagIdFollowed = tagIdFollowed }

        fun appReferral(appReferral: AppReferral?) = apply { this.appReferral = appReferral }

        override fun build(): Referrer = Referrer(url = this.url, component = this.component,
                linkName = this.linkName, platform = this.platform, viewId = this.viewId,
                email = this.email, nativeAppSource = this.nativeAppSource, google = this.google,
                tagIdFollowed = this.tagIdFollowed, appReferral = this.appReferral)
        override fun reset() {
            this.url = null
            this.component = null
            this.linkName = null
            this.platform = null
            this.viewId = null
            this.email = null
            this.nativeAppSource = null
            this.google = null
            this.tagIdFollowed = null
            this.appReferral = null
        }
    }

    private class ReferrerAdapter : Adapter<Referrer, Builder> {
        override fun read(protocol: Protocol) = read(protocol, Builder())

        override fun read(protocol: Protocol, builder: Builder): Referrer {
            protocol.readStructBegin()
            while (true) {
                val fieldMeta = protocol.readFieldBegin()
                if (fieldMeta.typeId == TType.STOP) {
                    break
                }
                when (fieldMeta.fieldId.toInt()) {
                    1 -> {
                        if (fieldMeta.typeId == TType.STRUCT) {
                            val url = Url.ADAPTER.read(protocol)
                            builder.url(url)
                        } else {
                            ProtocolUtil.skip(protocol, fieldMeta.typeId)
                        }
                    }
                    4 -> {
                        if (fieldMeta.typeId == TType.STRING) {
                            val component = protocol.readString()
                            builder.component(component)
                        } else {
                            ProtocolUtil.skip(protocol, fieldMeta.typeId)
                        }
                    }
                    10 -> {
                        if (fieldMeta.typeId == TType.STRUCT) {
                            val linkName = LinkName.ADAPTER.read(protocol)
                            builder.linkName(linkName)
                        } else {
                            ProtocolUtil.skip(protocol, fieldMeta.typeId)
                        }
                    }
                    5 -> {
                        if (fieldMeta.typeId == TType.I32) {
                            val platform = protocol.readI32().let {
                                Platform.findByValue(it) ?: throw ThriftException(ThriftException.Kind.PROTOCOL_ERROR, "Unexpected value for enum type Platform: $it")
                            }
                            builder.platform(platform)
                        } else {
                            ProtocolUtil.skip(protocol, fieldMeta.typeId)
                        }
                    }
                    6 -> {
                        if (fieldMeta.typeId == TType.STRING) {
                            val viewId = protocol.readString()
                            builder.viewId(viewId)
                        } else {
                            ProtocolUtil.skip(protocol, fieldMeta.typeId)
                        }
                    }
                    7 -> {
                        if (fieldMeta.typeId == TType.STRING) {
                            val email = protocol.readString()
                            builder.email(email)
                        } else {
                            ProtocolUtil.skip(protocol, fieldMeta.typeId)
                        }
                    }
                    8 -> {
                        if (fieldMeta.typeId == TType.I32) {
                            val nativeAppSource = protocol.readI32().let {
                                Source.findByValue(it) ?: throw ThriftException(ThriftException.Kind.PROTOCOL_ERROR, "Unexpected value for enum type Source: $it")
                            }
                            builder.nativeAppSource(nativeAppSource)
                        } else {
                            ProtocolUtil.skip(protocol, fieldMeta.typeId)
                        }
                    }
                    9 -> {
                        if (fieldMeta.typeId == TType.STRUCT) {
                            val google = GoogleReferral.ADAPTER.read(protocol)
                            builder.google(google)
                        } else {
                            ProtocolUtil.skip(protocol, fieldMeta.typeId)
                        }
                    }
                    11 -> {
                        if (fieldMeta.typeId == TType.STRING) {
                            val tagIdFollowed = protocol.readString()
                            builder.tagIdFollowed(tagIdFollowed)
                        } else {
                            ProtocolUtil.skip(protocol, fieldMeta.typeId)
                        }
                    }
                    12 -> {
                        if (fieldMeta.typeId == TType.STRUCT) {
                            val appReferral = AppReferral.ADAPTER.read(protocol)
                            builder.appReferral(appReferral)
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

        override fun write(protocol: Protocol, struct: Referrer) {
            protocol.writeStructBegin("Referrer")
            if (struct.url != null) {
                protocol.writeFieldBegin("url", 1, TType.STRUCT)
                Url.ADAPTER.write(protocol, struct.url)
                protocol.writeFieldEnd()
            }
            if (struct.component != null) {
                protocol.writeFieldBegin("component", 4, TType.STRING)
                protocol.writeString(struct.component)
                protocol.writeFieldEnd()
            }
            if (struct.linkName != null) {
                protocol.writeFieldBegin("linkName", 10, TType.STRUCT)
                LinkName.ADAPTER.write(protocol, struct.linkName)
                protocol.writeFieldEnd()
            }
            if (struct.platform != null) {
                protocol.writeFieldBegin("platform", 5, TType.I32)
                protocol.writeI32(struct.platform.value)
                protocol.writeFieldEnd()
            }
            if (struct.viewId != null) {
                protocol.writeFieldBegin("viewId", 6, TType.STRING)
                protocol.writeString(struct.viewId)
                protocol.writeFieldEnd()
            }
            if (struct.email != null) {
                protocol.writeFieldBegin("email", 7, TType.STRING)
                protocol.writeString(struct.email)
                protocol.writeFieldEnd()
            }
            if (struct.nativeAppSource != null) {
                protocol.writeFieldBegin("nativeAppSource", 8, TType.I32)
                protocol.writeI32(struct.nativeAppSource.value)
                protocol.writeFieldEnd()
            }
            if (struct.google != null) {
                protocol.writeFieldBegin("google", 9, TType.STRUCT)
                GoogleReferral.ADAPTER.write(protocol, struct.google)
                protocol.writeFieldEnd()
            }
            if (struct.tagIdFollowed != null) {
                protocol.writeFieldBegin("tagIdFollowed", 11, TType.STRING)
                protocol.writeString(struct.tagIdFollowed)
                protocol.writeFieldEnd()
            }
            if (struct.appReferral != null) {
                protocol.writeFieldBegin("appReferral", 12, TType.STRUCT)
                AppReferral.ADAPTER.write(protocol, struct.appReferral)
                protocol.writeFieldEnd()
            }
            protocol.writeFieldStop()
            protocol.writeStructEnd()
        }
    }

    companion object {
        @JvmField
        val ADAPTER: Adapter<Referrer, Builder> = ReferrerAdapter()
    }
}

/**
 * Represents a url either of a page served or a referrer
 */
data class Url(
    @JvmField @ThriftField(fieldId = 1, isRequired = true) val raw: String,
    @JvmField @ThriftField(fieldId = 2, isRequired = true) val host: String,
    @JvmField @ThriftField(fieldId = 6, isRequired = true) val domain: String,
    @JvmField @ThriftField(fieldId = 3, isRequired = true) val path: String,
    @JvmField @ThriftField(fieldId = 5, isOptional = true) val site: SignificantSite?,
    @JvmField @ThriftField(fieldId = 11, isOptional = true) val synthesised: Boolean? = false
) : Struct {
    override fun write(protocol: Protocol) {
        ADAPTER.write(protocol, this)
    }

    class Builder : StructBuilder<Url> {
        private var raw: String? = null

        private var host: String? = null

        private var domain: String? = null

        private var path: String? = null

        private var site: SignificantSite? = null

        private var synthesised: Boolean? = false

        constructor() {
            this.raw = null
            this.host = null
            this.domain = null
            this.path = null
            this.site = null
            this.synthesised = false
        }

        constructor(source: Url) {
            this.raw = source.raw
            this.host = source.host
            this.domain = source.domain
            this.path = source.path
            this.site = source.site
            this.synthesised = source.synthesised
        }

        fun raw(raw: String) = apply { this.raw = raw }

        fun host(host: String) = apply { this.host = host }

        fun domain(domain: String) = apply { this.domain = domain }

        fun path(path: String) = apply { this.path = path }

        fun site(site: SignificantSite?) = apply { this.site = site }

        fun synthesised(synthesised: Boolean?) = apply { this.synthesised = synthesised }

        override fun build(): Url = Url(raw = checkNotNull(raw) { "Required field 'raw' is missing" },
                host = checkNotNull(host) { "Required field 'host' is missing" },
                domain = checkNotNull(domain) { "Required field 'domain' is missing" },
                path = checkNotNull(path) { "Required field 'path' is missing" }, site = this.site,
                synthesised = this.synthesised)
        override fun reset() {
            this.raw = null
            this.host = null
            this.domain = null
            this.path = null
            this.site = null
            this.synthesised = false
        }
    }

    private class UrlAdapter : Adapter<Url, Builder> {
        override fun read(protocol: Protocol) = read(protocol, Builder())

        override fun read(protocol: Protocol, builder: Builder): Url {
            protocol.readStructBegin()
            while (true) {
                val fieldMeta = protocol.readFieldBegin()
                if (fieldMeta.typeId == TType.STOP) {
                    break
                }
                when (fieldMeta.fieldId.toInt()) {
                    1 -> {
                        if (fieldMeta.typeId == TType.STRING) {
                            val raw = protocol.readString()
                            builder.raw(raw)
                        } else {
                            ProtocolUtil.skip(protocol, fieldMeta.typeId)
                        }
                    }
                    2 -> {
                        if (fieldMeta.typeId == TType.STRING) {
                            val host = protocol.readString()
                            builder.host(host)
                        } else {
                            ProtocolUtil.skip(protocol, fieldMeta.typeId)
                        }
                    }
                    6 -> {
                        if (fieldMeta.typeId == TType.STRING) {
                            val domain = protocol.readString()
                            builder.domain(domain)
                        } else {
                            ProtocolUtil.skip(protocol, fieldMeta.typeId)
                        }
                    }
                    3 -> {
                        if (fieldMeta.typeId == TType.STRING) {
                            val path = protocol.readString()
                            builder.path(path)
                        } else {
                            ProtocolUtil.skip(protocol, fieldMeta.typeId)
                        }
                    }
                    5 -> {
                        if (fieldMeta.typeId == TType.I32) {
                            val site = protocol.readI32().let {
                                SignificantSite.findByValue(it) ?: throw ThriftException(ThriftException.Kind.PROTOCOL_ERROR, "Unexpected value for enum type SignificantSite: $it")
                            }
                            builder.site(site)
                        } else {
                            ProtocolUtil.skip(protocol, fieldMeta.typeId)
                        }
                    }
                    11 -> {
                        if (fieldMeta.typeId == TType.BOOL) {
                            val synthesised = protocol.readBool()
                            builder.synthesised(synthesised)
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

        override fun write(protocol: Protocol, struct: Url) {
            protocol.writeStructBegin("Url")
            protocol.writeFieldBegin("raw", 1, TType.STRING)
            protocol.writeString(struct.raw)
            protocol.writeFieldEnd()
            protocol.writeFieldBegin("host", 2, TType.STRING)
            protocol.writeString(struct.host)
            protocol.writeFieldEnd()
            protocol.writeFieldBegin("domain", 6, TType.STRING)
            protocol.writeString(struct.domain)
            protocol.writeFieldEnd()
            protocol.writeFieldBegin("path", 3, TType.STRING)
            protocol.writeString(struct.path)
            protocol.writeFieldEnd()
            if (struct.site != null) {
                protocol.writeFieldBegin("site", 5, TType.I32)
                protocol.writeI32(struct.site.value)
                protocol.writeFieldEnd()
            }
            if (struct.synthesised != null) {
                protocol.writeFieldBegin("synthesised", 11, TType.BOOL)
                protocol.writeBool(struct.synthesised)
                protocol.writeFieldEnd()
            }
            protocol.writeFieldStop()
            protocol.writeStructEnd()
        }
    }

    companion object {
        @JvmField
        val ADAPTER: Adapter<Url, Builder> = UrlAdapter()
    }
}

/**
 * Represents the position and location of a link within the Guardian site.
 * We hope to enhance this structure to include a better representation of what the links
 * actually mean, but for now we just report exactly what the web site tells us,
 * which is a hierarchical list of named items e.g.
 *  "more","container-2 | highlights","Front | /uk"
 *  "article","news | group-1+ | card-3","container-1 | headlines","Front | /uk"
 */
data class LinkName(@JvmField @ThriftField(fieldId = 1, isOptional = true) val raw: List<String>?) : Struct {
    override fun write(protocol: Protocol) {
        ADAPTER.write(protocol, this)
    }

    class Builder : StructBuilder<LinkName> {
        private var raw: List<String>? = null

        constructor() {
            this.raw = null
        }

        constructor(source: LinkName) {
            this.raw = source.raw
        }

        fun raw(raw: List<String>?) = apply { this.raw = raw }

        override fun build(): LinkName = LinkName(raw = this.raw)
        override fun reset() {
            this.raw = null
        }
    }

    private class LinkNameAdapter : Adapter<LinkName, Builder> {
        override fun read(protocol: Protocol) = read(protocol, Builder())

        override fun read(protocol: Protocol, builder: Builder): LinkName {
            protocol.readStructBegin()
            while (true) {
                val fieldMeta = protocol.readFieldBegin()
                if (fieldMeta.typeId == TType.STOP) {
                    break
                }
                when (fieldMeta.fieldId.toInt()) {
                    1 -> {
                        if (fieldMeta.typeId == TType.LIST) {
                            val list0 = protocol.readListBegin()
                            val raw = ArrayList<String>(list0.size)
                            for (i0 in 0 until list0.size) {
                                val item0 = protocol.readString()
                                raw += item0
                            }
                            protocol.readListEnd()
                            builder.raw(raw)
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

        override fun write(protocol: Protocol, struct: LinkName) {
            protocol.writeStructBegin("LinkName")
            if (struct.raw != null) {
                protocol.writeFieldBegin("raw", 1, TType.LIST)
                protocol.writeListBegin(TType.STRING, struct.raw.size)
                for (item0 in struct.raw) {
                    protocol.writeString(item0)
                }
                protocol.writeListEnd()
                protocol.writeFieldEnd()
            }
            protocol.writeFieldStop()
            protocol.writeStructEnd()
        }
    }

    companion object {
        @JvmField
        val ADAPTER: Adapter<LinkName, Builder> = LinkNameAdapter()
    }
}

/**
 * Where the referrer was google and they've provided additional information
 * on the query string, here is
 * that additional information.
 */
data class GoogleReferral(
    @JvmField @ThriftField(fieldId = 1, isOptional = true) val q: String?,
    @JvmField @ThriftField(fieldId = 2, isOptional = true) val rank: Int?,
    @JvmField @ThriftField(fieldId = 3, isOptional = true) val source: String?
) : Struct {
    override fun write(protocol: Protocol) {
        ADAPTER.write(protocol, this)
    }

    class Builder : StructBuilder<GoogleReferral> {
        private var q: String? = null

        private var rank: Int? = null

        private var source: String? = null

        constructor() {
            this.q = null
            this.rank = null
            this.source = null
        }

        constructor(source: GoogleReferral) {
            this.q = source.q
            this.rank = source.rank
            this.source = source.source
        }

        fun q(q: String?) = apply { this.q = q }

        fun rank(rank: Int?) = apply { this.rank = rank }

        fun source(source: String?) = apply { this.source = source }

        override fun build(): GoogleReferral = GoogleReferral(q = this.q, rank = this.rank,
                source = this.source)
        override fun reset() {
            this.q = null
            this.rank = null
            this.source = null
        }
    }

    private class GoogleReferralAdapter : Adapter<GoogleReferral, Builder> {
        override fun read(protocol: Protocol) = read(protocol, Builder())

        override fun read(protocol: Protocol, builder: Builder): GoogleReferral {
            protocol.readStructBegin()
            while (true) {
                val fieldMeta = protocol.readFieldBegin()
                if (fieldMeta.typeId == TType.STOP) {
                    break
                }
                when (fieldMeta.fieldId.toInt()) {
                    1 -> {
                        if (fieldMeta.typeId == TType.STRING) {
                            val q = protocol.readString()
                            builder.q(q)
                        } else {
                            ProtocolUtil.skip(protocol, fieldMeta.typeId)
                        }
                    }
                    2 -> {
                        if (fieldMeta.typeId == TType.I32) {
                            val rank = protocol.readI32()
                            builder.rank(rank)
                        } else {
                            ProtocolUtil.skip(protocol, fieldMeta.typeId)
                        }
                    }
                    3 -> {
                        if (fieldMeta.typeId == TType.STRING) {
                            val source = protocol.readString()
                            builder.source(source)
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

        override fun write(protocol: Protocol, struct: GoogleReferral) {
            protocol.writeStructBegin("GoogleReferral")
            if (struct.q != null) {
                protocol.writeFieldBegin("q", 1, TType.STRING)
                protocol.writeString(struct.q)
                protocol.writeFieldEnd()
            }
            if (struct.rank != null) {
                protocol.writeFieldBegin("rank", 2, TType.I32)
                protocol.writeI32(struct.rank)
                protocol.writeFieldEnd()
            }
            if (struct.source != null) {
                protocol.writeFieldBegin("source", 3, TType.STRING)
                protocol.writeString(struct.source)
                protocol.writeFieldEnd()
            }
            protocol.writeFieldStop()
            protocol.writeStructEnd()
        }
    }

    companion object {
        @JvmField
        val ADAPTER: Adapter<GoogleReferral, Builder> = GoogleReferralAdapter()
    }
}

/**
 * Details about media playback progress
 * Note: currently only guardian-hosted videos are reported.
 */
data class MediaPlayback(
    @JvmField @ThriftField(fieldId = 1, isRequired = true) val mediaId: String,
    @JvmField @ThriftField(fieldId = 2, isRequired = true) val mediaType: MediaType,
    @JvmField @ThriftField(fieldId = 3, isRequired = true) val preroll: Boolean,
    @JvmField @ThriftField(fieldId = 4, isRequired = true) val eventType: MediaEvent
) : Struct {
    override fun write(protocol: Protocol) {
        ADAPTER.write(protocol, this)
    }

    class Builder : StructBuilder<MediaPlayback> {
        private var mediaId: String? = null

        private var mediaType: MediaType? = null

        private var preroll: Boolean? = null

        private var eventType: MediaEvent? = null

        constructor() {
            this.mediaId = null
            this.mediaType = null
            this.preroll = null
            this.eventType = null
        }

        constructor(source: MediaPlayback) {
            this.mediaId = source.mediaId
            this.mediaType = source.mediaType
            this.preroll = source.preroll
            this.eventType = source.eventType
        }

        fun mediaId(mediaId: String) = apply { this.mediaId = mediaId }

        fun mediaType(mediaType: MediaType) = apply { this.mediaType = mediaType }

        fun preroll(preroll: Boolean) = apply { this.preroll = preroll }

        fun eventType(eventType: MediaEvent) = apply { this.eventType = eventType }

        override fun build(): MediaPlayback = MediaPlayback(mediaId = checkNotNull(mediaId) { "Required field 'mediaId' is missing" },
                mediaType = checkNotNull(mediaType) { "Required field 'mediaType' is missing" },
                preroll = checkNotNull(preroll) { "Required field 'preroll' is missing" },
                eventType = checkNotNull(eventType) { "Required field 'eventType' is missing" })
        override fun reset() {
            this.mediaId = null
            this.mediaType = null
            this.preroll = null
            this.eventType = null
        }
    }

    private class MediaPlaybackAdapter : Adapter<MediaPlayback, Builder> {
        override fun read(protocol: Protocol) = read(protocol, Builder())

        override fun read(protocol: Protocol, builder: Builder): MediaPlayback {
            protocol.readStructBegin()
            while (true) {
                val fieldMeta = protocol.readFieldBegin()
                if (fieldMeta.typeId == TType.STOP) {
                    break
                }
                when (fieldMeta.fieldId.toInt()) {
                    1 -> {
                        if (fieldMeta.typeId == TType.STRING) {
                            val mediaId = protocol.readString()
                            builder.mediaId(mediaId)
                        } else {
                            ProtocolUtil.skip(protocol, fieldMeta.typeId)
                        }
                    }
                    2 -> {
                        if (fieldMeta.typeId == TType.I32) {
                            val mediaType = protocol.readI32().let {
                                MediaType.findByValue(it) ?: throw ThriftException(ThriftException.Kind.PROTOCOL_ERROR, "Unexpected value for enum type MediaType: $it")
                            }
                            builder.mediaType(mediaType)
                        } else {
                            ProtocolUtil.skip(protocol, fieldMeta.typeId)
                        }
                    }
                    3 -> {
                        if (fieldMeta.typeId == TType.BOOL) {
                            val preroll = protocol.readBool()
                            builder.preroll(preroll)
                        } else {
                            ProtocolUtil.skip(protocol, fieldMeta.typeId)
                        }
                    }
                    4 -> {
                        if (fieldMeta.typeId == TType.I32) {
                            val eventType = protocol.readI32().let {
                                MediaEvent.findByValue(it) ?: throw ThriftException(ThriftException.Kind.PROTOCOL_ERROR, "Unexpected value for enum type MediaEvent: $it")
                            }
                            builder.eventType(eventType)
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

        override fun write(protocol: Protocol, struct: MediaPlayback) {
            protocol.writeStructBegin("MediaPlayback")
            protocol.writeFieldBegin("mediaId", 1, TType.STRING)
            protocol.writeString(struct.mediaId)
            protocol.writeFieldEnd()
            protocol.writeFieldBegin("mediaType", 2, TType.I32)
            protocol.writeI32(struct.mediaType.value)
            protocol.writeFieldEnd()
            protocol.writeFieldBegin("preroll", 3, TType.BOOL)
            protocol.writeBool(struct.preroll)
            protocol.writeFieldEnd()
            protocol.writeFieldBegin("eventType", 4, TType.I32)
            protocol.writeI32(struct.eventType.value)
            protocol.writeFieldEnd()
            protocol.writeFieldStop()
            protocol.writeStructEnd()
        }
    }

    companion object {
        @JvmField
        val ADAPTER: Adapter<MediaPlayback, Builder> = MediaPlaybackAdapter()
    }
}

/**
 * Represents monetary contribution made by a reader via this page.
 */
data class Acquisition(
    @JvmField @ThriftField(fieldId = 1, isRequired = true) val product: Product,
    @JvmField @ThriftField(fieldId = 2, isRequired = true) val paymentFrequency: PaymentFrequency,
    @JvmField @ThriftField(fieldId = 3, isRequired = true) val currency: String,
    @JvmField @ThriftField(fieldId = 4, isRequired = true) val amount: Double,
    @JvmField @ThriftField(fieldId = 6, isOptional = true) val paymentProvider: PaymentProvider?,
    @JvmField @ThriftField(fieldId = 7, isOptional = true) val campaignCode: Set<String>?,
    @JvmField @ThriftField(fieldId = 8, isOptional = true) val abTests: AbTestInfo?,
    @JvmField @ThriftField(fieldId = 9, isOptional = true) val countryCode: String?,
    @JvmField @ThriftField(fieldId = 10, isOptional = true) val referrerPageViewId: String?,
    @JvmField @ThriftField(fieldId = 11, isOptional = true) val referrerUrl: String?,
    @JvmField @ThriftField(fieldId = 12, isOptional = true) val componentId: String?,
    @JvmField @ThriftField(fieldId = 14, isOptional = true) val componentTypeV2: ComponentType?,
    @JvmField @ThriftField(fieldId = 15, isOptional = true) val source: AcquisitionSource?,
    @JvmField @ThriftField(fieldId = 16, isOptional = true) val printOptions: PrintOptions?,
    @JvmField @ThriftField(fieldId = 19, isOptional = true) val platform: Platform?,
    @JvmField @ThriftField(fieldId = 20, isOptional = true) val discountLengthInMonths: Short?,
    @JvmField @ThriftField(fieldId = 21, isOptional = true) val discountPercentage: Double?,
    @JvmField @ThriftField(fieldId = 22, isOptional = true) val promoCode: String?,
    @JvmField @ThriftField(fieldId = 23, isOptional = true) val labels: Set<String>?,
    @JvmField @ThriftField(fieldId = 24, isOptional = true) val identityId: String?,
    @JvmField @ThriftField(fieldId = 25, isOptional = true) val queryParameters: Set<QueryParameter>?
) : Struct {
    override fun write(protocol: Protocol) {
        ADAPTER.write(protocol, this)
    }

    class Builder : StructBuilder<Acquisition> {
        private var product: Product? = null

        private var paymentFrequency: PaymentFrequency? = null

        private var currency: String? = null

        private var amount: Double? = null

        private var paymentProvider: PaymentProvider? = null

        private var campaignCode: Set<String>? = null

        private var abTests: AbTestInfo? = null

        private var countryCode: String? = null

        private var referrerPageViewId: String? = null

        private var referrerUrl: String? = null

        private var componentId: String? = null

        private var componentTypeV2: ComponentType? = null

        private var source: AcquisitionSource? = null

        private var printOptions: PrintOptions? = null

        private var platform: Platform? = null

        private var discountLengthInMonths: Short? = null

        private var discountPercentage: Double? = null

        private var promoCode: String? = null

        private var labels: Set<String>? = null

        private var identityId: String? = null

        private var queryParameters: Set<QueryParameter>? = null

        constructor() {
            this.product = null
            this.paymentFrequency = null
            this.currency = null
            this.amount = null
            this.paymentProvider = null
            this.campaignCode = null
            this.abTests = null
            this.countryCode = null
            this.referrerPageViewId = null
            this.referrerUrl = null
            this.componentId = null
            this.componentTypeV2 = null
            this.source = null
            this.printOptions = null
            this.platform = null
            this.discountLengthInMonths = null
            this.discountPercentage = null
            this.promoCode = null
            this.labels = null
            this.identityId = null
            this.queryParameters = null
        }

        constructor(source: Acquisition) {
            this.product = source.product
            this.paymentFrequency = source.paymentFrequency
            this.currency = source.currency
            this.amount = source.amount
            this.paymentProvider = source.paymentProvider
            this.campaignCode = source.campaignCode
            this.abTests = source.abTests
            this.countryCode = source.countryCode
            this.referrerPageViewId = source.referrerPageViewId
            this.referrerUrl = source.referrerUrl
            this.componentId = source.componentId
            this.componentTypeV2 = source.componentTypeV2
            this.source = source.source
            this.printOptions = source.printOptions
            this.platform = source.platform
            this.discountLengthInMonths = source.discountLengthInMonths
            this.discountPercentage = source.discountPercentage
            this.promoCode = source.promoCode
            this.labels = source.labels
            this.identityId = source.identityId
            this.queryParameters = source.queryParameters
        }

        fun product(product: Product) = apply { this.product = product }

        fun paymentFrequency(paymentFrequency: PaymentFrequency) = apply { this.paymentFrequency = paymentFrequency }

        fun currency(currency: String) = apply { this.currency = currency }

        fun amount(amount: Double) = apply { this.amount = amount }

        fun paymentProvider(paymentProvider: PaymentProvider?) = apply { this.paymentProvider = paymentProvider }

        fun campaignCode(campaignCode: Set<String>?) = apply { this.campaignCode = campaignCode }

        fun abTests(abTests: AbTestInfo?) = apply { this.abTests = abTests }

        fun countryCode(countryCode: String?) = apply { this.countryCode = countryCode }

        fun referrerPageViewId(referrerPageViewId: String?) = apply { this.referrerPageViewId = referrerPageViewId }

        fun referrerUrl(referrerUrl: String?) = apply { this.referrerUrl = referrerUrl }

        fun componentId(componentId: String?) = apply { this.componentId = componentId }

        fun componentTypeV2(componentTypeV2: ComponentType?) = apply { this.componentTypeV2 = componentTypeV2 }

        fun source(source: AcquisitionSource?) = apply { this.source = source }

        fun printOptions(printOptions: PrintOptions?) = apply { this.printOptions = printOptions }

        fun platform(platform: Platform?) = apply { this.platform = platform }

        fun discountLengthInMonths(discountLengthInMonths: Short?) = apply { this.discountLengthInMonths = discountLengthInMonths }

        fun discountPercentage(discountPercentage: Double?) = apply { this.discountPercentage = discountPercentage }

        fun promoCode(promoCode: String?) = apply { this.promoCode = promoCode }

        fun labels(labels: Set<String>?) = apply { this.labels = labels }

        fun identityId(identityId: String?) = apply { this.identityId = identityId }

        fun queryParameters(queryParameters: Set<QueryParameter>?) = apply { this.queryParameters = queryParameters }

        override fun build(): Acquisition = Acquisition(product = checkNotNull(product) { "Required field 'product' is missing" },
                paymentFrequency = checkNotNull(paymentFrequency) { "Required field 'paymentFrequency' is missing" },
                currency = checkNotNull(currency) { "Required field 'currency' is missing" },
                amount = checkNotNull(amount) { "Required field 'amount' is missing" },
                paymentProvider = this.paymentProvider, campaignCode = this.campaignCode,
                abTests = this.abTests, countryCode = this.countryCode,
                referrerPageViewId = this.referrerPageViewId, referrerUrl = this.referrerUrl,
                componentId = this.componentId, componentTypeV2 = this.componentTypeV2,
                source = this.source, printOptions = this.printOptions, platform = this.platform,
                discountLengthInMonths = this.discountLengthInMonths,
                discountPercentage = this.discountPercentage, promoCode = this.promoCode,
                labels = this.labels, identityId = this.identityId,
                queryParameters = this.queryParameters)
        override fun reset() {
            this.product = null
            this.paymentFrequency = null
            this.currency = null
            this.amount = null
            this.paymentProvider = null
            this.campaignCode = null
            this.abTests = null
            this.countryCode = null
            this.referrerPageViewId = null
            this.referrerUrl = null
            this.componentId = null
            this.componentTypeV2 = null
            this.source = null
            this.printOptions = null
            this.platform = null
            this.discountLengthInMonths = null
            this.discountPercentage = null
            this.promoCode = null
            this.labels = null
            this.identityId = null
            this.queryParameters = null
        }
    }

    private class AcquisitionAdapter : Adapter<Acquisition, Builder> {
        override fun read(protocol: Protocol) = read(protocol, Builder())

        override fun read(protocol: Protocol, builder: Builder): Acquisition {
            protocol.readStructBegin()
            while (true) {
                val fieldMeta = protocol.readFieldBegin()
                if (fieldMeta.typeId == TType.STOP) {
                    break
                }
                when (fieldMeta.fieldId.toInt()) {
                    1 -> {
                        if (fieldMeta.typeId == TType.I32) {
                            val product = protocol.readI32().let {
                                Product.findByValue(it) ?: throw ThriftException(ThriftException.Kind.PROTOCOL_ERROR, "Unexpected value for enum type Product: $it")
                            }
                            builder.product(product)
                        } else {
                            ProtocolUtil.skip(protocol, fieldMeta.typeId)
                        }
                    }
                    2 -> {
                        if (fieldMeta.typeId == TType.I32) {
                            val paymentFrequency = protocol.readI32().let {
                                PaymentFrequency.findByValue(it) ?: throw ThriftException(ThriftException.Kind.PROTOCOL_ERROR, "Unexpected value for enum type PaymentFrequency: $it")
                            }
                            builder.paymentFrequency(paymentFrequency)
                        } else {
                            ProtocolUtil.skip(protocol, fieldMeta.typeId)
                        }
                    }
                    3 -> {
                        if (fieldMeta.typeId == TType.STRING) {
                            val currency = protocol.readString()
                            builder.currency(currency)
                        } else {
                            ProtocolUtil.skip(protocol, fieldMeta.typeId)
                        }
                    }
                    4 -> {
                        if (fieldMeta.typeId == TType.DOUBLE) {
                            val amount = protocol.readDouble()
                            builder.amount(amount)
                        } else {
                            ProtocolUtil.skip(protocol, fieldMeta.typeId)
                        }
                    }
                    6 -> {
                        if (fieldMeta.typeId == TType.I32) {
                            val paymentProvider = protocol.readI32().let {
                                PaymentProvider.findByValue(it) ?: throw ThriftException(ThriftException.Kind.PROTOCOL_ERROR, "Unexpected value for enum type PaymentProvider: $it")
                            }
                            builder.paymentProvider(paymentProvider)
                        } else {
                            ProtocolUtil.skip(protocol, fieldMeta.typeId)
                        }
                    }
                    7 -> {
                        if (fieldMeta.typeId == TType.SET) {
                            val set0 = protocol.readSetBegin()
                            val campaignCode = LinkedHashSet<String>(set0.size)
                            for (i0 in 0 until set0.size) {
                                val item0 = protocol.readString()
                                campaignCode += item0
                            }
                            protocol.readSetEnd()
                            builder.campaignCode(campaignCode)
                        } else {
                            ProtocolUtil.skip(protocol, fieldMeta.typeId)
                        }
                    }
                    8 -> {
                        if (fieldMeta.typeId == TType.STRUCT) {
                            val abTests = AbTestInfo.ADAPTER.read(protocol)
                            builder.abTests(abTests)
                        } else {
                            ProtocolUtil.skip(protocol, fieldMeta.typeId)
                        }
                    }
                    9 -> {
                        if (fieldMeta.typeId == TType.STRING) {
                            val countryCode = protocol.readString()
                            builder.countryCode(countryCode)
                        } else {
                            ProtocolUtil.skip(protocol, fieldMeta.typeId)
                        }
                    }
                    10 -> {
                        if (fieldMeta.typeId == TType.STRING) {
                            val referrerPageViewId = protocol.readString()
                            builder.referrerPageViewId(referrerPageViewId)
                        } else {
                            ProtocolUtil.skip(protocol, fieldMeta.typeId)
                        }
                    }
                    11 -> {
                        if (fieldMeta.typeId == TType.STRING) {
                            val referrerUrl = protocol.readString()
                            builder.referrerUrl(referrerUrl)
                        } else {
                            ProtocolUtil.skip(protocol, fieldMeta.typeId)
                        }
                    }
                    12 -> {
                        if (fieldMeta.typeId == TType.STRING) {
                            val componentId = protocol.readString()
                            builder.componentId(componentId)
                        } else {
                            ProtocolUtil.skip(protocol, fieldMeta.typeId)
                        }
                    }
                    14 -> {
                        if (fieldMeta.typeId == TType.I32) {
                            val componentTypeV2 = protocol.readI32().let {
                                ComponentType.findByValue(it) ?: throw ThriftException(ThriftException.Kind.PROTOCOL_ERROR, "Unexpected value for enum type ComponentType: $it")
                            }
                            builder.componentTypeV2(componentTypeV2)
                        } else {
                            ProtocolUtil.skip(protocol, fieldMeta.typeId)
                        }
                    }
                    15 -> {
                        if (fieldMeta.typeId == TType.I32) {
                            val source = protocol.readI32().let {
                                AcquisitionSource.findByValue(it) ?: throw ThriftException(ThriftException.Kind.PROTOCOL_ERROR, "Unexpected value for enum type AcquisitionSource: $it")
                            }
                            builder.source(source)
                        } else {
                            ProtocolUtil.skip(protocol, fieldMeta.typeId)
                        }
                    }
                    16 -> {
                        if (fieldMeta.typeId == TType.STRUCT) {
                            val printOptions = PrintOptions.ADAPTER.read(protocol)
                            builder.printOptions(printOptions)
                        } else {
                            ProtocolUtil.skip(protocol, fieldMeta.typeId)
                        }
                    }
                    19 -> {
                        if (fieldMeta.typeId == TType.I32) {
                            val platform = protocol.readI32().let {
                                Platform.findByValue(it) ?: throw ThriftException(ThriftException.Kind.PROTOCOL_ERROR, "Unexpected value for enum type Platform: $it")
                            }
                            builder.platform(platform)
                        } else {
                            ProtocolUtil.skip(protocol, fieldMeta.typeId)
                        }
                    }
                    20 -> {
                        if (fieldMeta.typeId == TType.I16) {
                            val discountLengthInMonths = protocol.readI16()
                            builder.discountLengthInMonths(discountLengthInMonths)
                        } else {
                            ProtocolUtil.skip(protocol, fieldMeta.typeId)
                        }
                    }
                    21 -> {
                        if (fieldMeta.typeId == TType.DOUBLE) {
                            val discountPercentage = protocol.readDouble()
                            builder.discountPercentage(discountPercentage)
                        } else {
                            ProtocolUtil.skip(protocol, fieldMeta.typeId)
                        }
                    }
                    22 -> {
                        if (fieldMeta.typeId == TType.STRING) {
                            val promoCode = protocol.readString()
                            builder.promoCode(promoCode)
                        } else {
                            ProtocolUtil.skip(protocol, fieldMeta.typeId)
                        }
                    }
                    23 -> {
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
                    24 -> {
                        if (fieldMeta.typeId == TType.STRING) {
                            val identityId = protocol.readString()
                            builder.identityId(identityId)
                        } else {
                            ProtocolUtil.skip(protocol, fieldMeta.typeId)
                        }
                    }
                    25 -> {
                        if (fieldMeta.typeId == TType.SET) {
                            val set0 = protocol.readSetBegin()
                            val queryParameters = LinkedHashSet<QueryParameter>(set0.size)
                            for (i0 in 0 until set0.size) {
                                val item0 = QueryParameter.ADAPTER.read(protocol)
                                queryParameters += item0
                            }
                            protocol.readSetEnd()
                            builder.queryParameters(queryParameters)
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

        override fun write(protocol: Protocol, struct: Acquisition) {
            protocol.writeStructBegin("Acquisition")
            protocol.writeFieldBegin("product", 1, TType.I32)
            protocol.writeI32(struct.product.value)
            protocol.writeFieldEnd()
            protocol.writeFieldBegin("paymentFrequency", 2, TType.I32)
            protocol.writeI32(struct.paymentFrequency.value)
            protocol.writeFieldEnd()
            protocol.writeFieldBegin("currency", 3, TType.STRING)
            protocol.writeString(struct.currency)
            protocol.writeFieldEnd()
            protocol.writeFieldBegin("amount", 4, TType.DOUBLE)
            protocol.writeDouble(struct.amount)
            protocol.writeFieldEnd()
            if (struct.paymentProvider != null) {
                protocol.writeFieldBegin("paymentProvider", 6, TType.I32)
                protocol.writeI32(struct.paymentProvider.value)
                protocol.writeFieldEnd()
            }
            if (struct.campaignCode != null) {
                protocol.writeFieldBegin("campaignCode", 7, TType.SET)
                protocol.writeSetBegin(TType.STRING, struct.campaignCode.size)
                for (item0 in struct.campaignCode) {
                    protocol.writeString(item0)
                }
                protocol.writeSetEnd()
                protocol.writeFieldEnd()
            }
            if (struct.abTests != null) {
                protocol.writeFieldBegin("abTests", 8, TType.STRUCT)
                AbTestInfo.ADAPTER.write(protocol, struct.abTests)
                protocol.writeFieldEnd()
            }
            if (struct.countryCode != null) {
                protocol.writeFieldBegin("countryCode", 9, TType.STRING)
                protocol.writeString(struct.countryCode)
                protocol.writeFieldEnd()
            }
            if (struct.referrerPageViewId != null) {
                protocol.writeFieldBegin("referrerPageViewId", 10, TType.STRING)
                protocol.writeString(struct.referrerPageViewId)
                protocol.writeFieldEnd()
            }
            if (struct.referrerUrl != null) {
                protocol.writeFieldBegin("referrerUrl", 11, TType.STRING)
                protocol.writeString(struct.referrerUrl)
                protocol.writeFieldEnd()
            }
            if (struct.componentId != null) {
                protocol.writeFieldBegin("componentId", 12, TType.STRING)
                protocol.writeString(struct.componentId)
                protocol.writeFieldEnd()
            }
            if (struct.componentTypeV2 != null) {
                protocol.writeFieldBegin("componentTypeV2", 14, TType.I32)
                protocol.writeI32(struct.componentTypeV2.value)
                protocol.writeFieldEnd()
            }
            if (struct.source != null) {
                protocol.writeFieldBegin("source", 15, TType.I32)
                protocol.writeI32(struct.source.value)
                protocol.writeFieldEnd()
            }
            if (struct.printOptions != null) {
                protocol.writeFieldBegin("printOptions", 16, TType.STRUCT)
                PrintOptions.ADAPTER.write(protocol, struct.printOptions)
                protocol.writeFieldEnd()
            }
            if (struct.platform != null) {
                protocol.writeFieldBegin("platform", 19, TType.I32)
                protocol.writeI32(struct.platform.value)
                protocol.writeFieldEnd()
            }
            if (struct.discountLengthInMonths != null) {
                protocol.writeFieldBegin("discountLengthInMonths", 20, TType.I16)
                protocol.writeI16(struct.discountLengthInMonths)
                protocol.writeFieldEnd()
            }
            if (struct.discountPercentage != null) {
                protocol.writeFieldBegin("discountPercentage", 21, TType.DOUBLE)
                protocol.writeDouble(struct.discountPercentage)
                protocol.writeFieldEnd()
            }
            if (struct.promoCode != null) {
                protocol.writeFieldBegin("promoCode", 22, TType.STRING)
                protocol.writeString(struct.promoCode)
                protocol.writeFieldEnd()
            }
            if (struct.labels != null) {
                protocol.writeFieldBegin("labels", 23, TType.SET)
                protocol.writeSetBegin(TType.STRING, struct.labels.size)
                for (item0 in struct.labels) {
                    protocol.writeString(item0)
                }
                protocol.writeSetEnd()
                protocol.writeFieldEnd()
            }
            if (struct.identityId != null) {
                protocol.writeFieldBegin("identityId", 24, TType.STRING)
                protocol.writeString(struct.identityId)
                protocol.writeFieldEnd()
            }
            if (struct.queryParameters != null) {
                protocol.writeFieldBegin("queryParameters", 25, TType.SET)
                protocol.writeSetBegin(TType.STRUCT, struct.queryParameters.size)
                for (item0 in struct.queryParameters) {
                    QueryParameter.ADAPTER.write(protocol, item0)
                }
                protocol.writeSetEnd()
                protocol.writeFieldEnd()
            }
            protocol.writeFieldStop()
            protocol.writeStructEnd()
        }
    }

    companion object {
        @JvmField
        val ADAPTER: Adapter<Acquisition, Builder> = AcquisitionAdapter()
    }
}

/**
 * Extra parameters only applicable to print products
 */
data class PrintOptions(@JvmField @ThriftField(fieldId = 1, isRequired = true) val product: PrintProduct, @JvmField @ThriftField(fieldId = 2, isRequired = true) val deliveryCountryCode: String) : Struct {
    override fun write(protocol: Protocol) {
        ADAPTER.write(protocol, this)
    }

    class Builder : StructBuilder<PrintOptions> {
        private var product: PrintProduct? = null

        private var deliveryCountryCode: String? = null

        constructor() {
            this.product = null
            this.deliveryCountryCode = null
        }

        constructor(source: PrintOptions) {
            this.product = source.product
            this.deliveryCountryCode = source.deliveryCountryCode
        }

        fun product(product: PrintProduct) = apply { this.product = product }

        fun deliveryCountryCode(deliveryCountryCode: String) = apply { this.deliveryCountryCode = deliveryCountryCode }

        override fun build(): PrintOptions = PrintOptions(product = checkNotNull(product) { "Required field 'product' is missing" },
                deliveryCountryCode = checkNotNull(deliveryCountryCode) { "Required field 'deliveryCountryCode' is missing" })
        override fun reset() {
            this.product = null
            this.deliveryCountryCode = null
        }
    }

    private class PrintOptionsAdapter : Adapter<PrintOptions, Builder> {
        override fun read(protocol: Protocol) = read(protocol, Builder())

        override fun read(protocol: Protocol, builder: Builder): PrintOptions {
            protocol.readStructBegin()
            while (true) {
                val fieldMeta = protocol.readFieldBegin()
                if (fieldMeta.typeId == TType.STOP) {
                    break
                }
                when (fieldMeta.fieldId.toInt()) {
                    1 -> {
                        if (fieldMeta.typeId == TType.I32) {
                            val product = protocol.readI32().let {
                                PrintProduct.findByValue(it) ?: throw ThriftException(ThriftException.Kind.PROTOCOL_ERROR, "Unexpected value for enum type PrintProduct: $it")
                            }
                            builder.product(product)
                        } else {
                            ProtocolUtil.skip(protocol, fieldMeta.typeId)
                        }
                    }
                    2 -> {
                        if (fieldMeta.typeId == TType.STRING) {
                            val deliveryCountryCode = protocol.readString()
                            builder.deliveryCountryCode(deliveryCountryCode)
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

        override fun write(protocol: Protocol, struct: PrintOptions) {
            protocol.writeStructBegin("PrintOptions")
            protocol.writeFieldBegin("product", 1, TType.I32)
            protocol.writeI32(struct.product.value)
            protocol.writeFieldEnd()
            protocol.writeFieldBegin("deliveryCountryCode", 2, TType.STRING)
            protocol.writeString(struct.deliveryCountryCode)
            protocol.writeFieldEnd()
            protocol.writeFieldStop()
            protocol.writeStructEnd()
        }
    }

    companion object {
        @JvmField
        val ADAPTER: Adapter<PrintOptions, Builder> = PrintOptionsAdapter()
    }
}

/**
 * A query string parameter
 */
data class QueryParameter(@JvmField @ThriftField(fieldId = 1, isRequired = true) val name: String, @JvmField @ThriftField(fieldId = 2, isRequired = true) val value: String) : Struct {
    override fun write(protocol: Protocol) {
        ADAPTER.write(protocol, this)
    }

    class Builder : StructBuilder<QueryParameter> {
        private var name: String? = null

        private var value: String? = null

        constructor() {
            this.name = null
            this.value = null
        }

        constructor(source: QueryParameter) {
            this.name = source.name
            this.value = source.value
        }

        fun name(name: String) = apply { this.name = name }

        fun value(value: String) = apply { this.value = value }

        override fun build(): QueryParameter = QueryParameter(name = checkNotNull(name) { "Required field 'name' is missing" },
                value = checkNotNull(value) { "Required field 'value' is missing" })
        override fun reset() {
            this.name = null
            this.value = null
        }
    }

    private class QueryParameterAdapter : Adapter<QueryParameter, Builder> {
        override fun read(protocol: Protocol) = read(protocol, Builder())

        override fun read(protocol: Protocol, builder: Builder): QueryParameter {
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
                            val value = protocol.readString()
                            builder.value(value)
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

        override fun write(protocol: Protocol, struct: QueryParameter) {
            protocol.writeStructBegin("QueryParameter")
            protocol.writeFieldBegin("name", 1, TType.STRING)
            protocol.writeString(struct.name)
            protocol.writeFieldEnd()
            protocol.writeFieldBegin("value", 2, TType.STRING)
            protocol.writeString(struct.value)
            protocol.writeFieldEnd()
            protocol.writeFieldStop()
            protocol.writeStructEnd()
        }
    }

    companion object {
        @JvmField
        val ADAPTER: Adapter<QueryParameter, Builder> = QueryParameterAdapter()
    }
}

data class InPageClick(@JvmField @ThriftField(fieldId = 1, isOptional = true) val component: String?, @JvmField @ThriftField(fieldId = 2, isOptional = true) val linkName: LinkName?) : Struct {
    override fun write(protocol: Protocol) {
        ADAPTER.write(protocol, this)
    }

    class Builder : StructBuilder<InPageClick> {
        private var component: String? = null

        private var linkName: LinkName? = null

        constructor() {
            this.component = null
            this.linkName = null
        }

        constructor(source: InPageClick) {
            this.component = source.component
            this.linkName = source.linkName
        }

        fun component(component: String?) = apply { this.component = component }

        fun linkName(linkName: LinkName?) = apply { this.linkName = linkName }

        override fun build(): InPageClick = InPageClick(component = this.component,
                linkName = this.linkName)
        override fun reset() {
            this.component = null
            this.linkName = null
        }
    }

    private class InPageClickAdapter : Adapter<InPageClick, Builder> {
        override fun read(protocol: Protocol) = read(protocol, Builder())

        override fun read(protocol: Protocol, builder: Builder): InPageClick {
            protocol.readStructBegin()
            while (true) {
                val fieldMeta = protocol.readFieldBegin()
                if (fieldMeta.typeId == TType.STOP) {
                    break
                }
                when (fieldMeta.fieldId.toInt()) {
                    1 -> {
                        if (fieldMeta.typeId == TType.STRING) {
                            val component = protocol.readString()
                            builder.component(component)
                        } else {
                            ProtocolUtil.skip(protocol, fieldMeta.typeId)
                        }
                    }
                    2 -> {
                        if (fieldMeta.typeId == TType.STRUCT) {
                            val linkName = LinkName.ADAPTER.read(protocol)
                            builder.linkName(linkName)
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

        override fun write(protocol: Protocol, struct: InPageClick) {
            protocol.writeStructBegin("InPageClick")
            if (struct.component != null) {
                protocol.writeFieldBegin("component", 1, TType.STRING)
                protocol.writeString(struct.component)
                protocol.writeFieldEnd()
            }
            if (struct.linkName != null) {
                protocol.writeFieldBegin("linkName", 2, TType.STRUCT)
                LinkName.ADAPTER.write(protocol, struct.linkName)
                protocol.writeFieldEnd()
            }
            protocol.writeFieldStop()
            protocol.writeStructEnd()
        }
    }

    companion object {
        @JvmField
        val ADAPTER: Adapter<InPageClick, Builder> = InPageClickAdapter()
    }
}
