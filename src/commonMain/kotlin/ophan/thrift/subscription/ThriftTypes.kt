package ophan.thrift.subscription

import kotlin.Int
import kotlin.jvm.JvmField
import kotlin.jvm.JvmStatic

/**
 * What type of subscription the user has (or had) for the app
 */
enum class SubscriptionType(@JvmField val value: Int) {
    /**
     * No subscription in place, and no previous record of a subscription on this device.
     */
    FREE(1),

    /**
     * User has an active subscription via the device's store.
     *
     * In json, "play" or "apple" are supported as synonyms of this value.
     */
    STORE(2),

    /**
     * User as an active subscription as part of a print bundle
     */
    PRINT(4),

    /**
     * No subscription in place, but user did previously have a subscription via the
     * device's store which is now expired.
     *
     * In json, "play:expired" and "apple:expired" are supported as synonyms of this value.
     */
    FREE_WITH_EXPIRED_STORE(3),

    /**
     * No subscription in place, but user did previously have a subscription as
     * part of a print bundle which is now expired.
     *
     * In json, "print:expired" is supported as a synonym of this value.
     */
    FREE_WITH_EXPIRED_PRINT(5),

    /**
     * User has an active digital pack subscription.
     */
    DIGITAL_PACK(6);

    companion object {
        @JvmStatic
        fun findByValue(value: Int): SubscriptionType? = when (value) {
            1 -> FREE
            2 -> STORE
            4 -> PRINT
            3 -> FREE_WITH_EXPIRED_STORE
            5 -> FREE_WITH_EXPIRED_PRINT
            6 -> DIGITAL_PACK
            else -> null
        }
    }
}

/**
 * What tier of membership the user is currently part of.
 */
enum class MembershipTier(@JvmField val value: Int) {
    /**
     * The user is a friend.
     */
    FRIEND(1),

    /**
     * The user is a Guardian employee.
     */
    STAFF(2),

    /**
     * The user is a supporter.
     */
    SUPPORTER(3),

    /**
     * The user is a partner.
     */
    PARTNER(4),

    /**
     * The user is a patron.
     */
    PATRON(5);

    companion object {
        @JvmStatic
        fun findByValue(value: Int): MembershipTier? = when (value) {
            1 -> FRIEND
            2 -> STAFF
            3 -> SUPPORTER
            4 -> PARTNER
            5 -> PATRON
            else -> null
        }
    }
}
