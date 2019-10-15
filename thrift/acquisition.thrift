namespace * ophan.thrift.event
namespace cocoa GLAOphanThriftEvent

include "abtest.thrift"
include "componentevent.thrift"
include "platform.thrift"
include "product.thrift"
include "printoptions.thrift"
include "queryparameter.thrift"

enum PaymentFrequency {

    ONE_OFF = 1

    MONTHLY = 2

    ANNUALLY = 3

    QUARTERLY = 4

    SIX_MONTHLY = 5

}

enum PaymentProvider {

    STRIPE = 1

    PAYPAL = 2

    GOCARDLESS = 3

    IN_APP_PURCHASE = 4

    STRIPE_APPLE_PAY = 5

    STRIPE_PAYMENT_REQUEST_BUTTON = 6

    SUBSCRIBE_WITH_GOOGLE = 7

}

enum AcquisitionSource {

    GUARDIAN_WEB = 1

    /**
    * DEPRECATED DO NOT USE
    **/
    GUARDIAN_APPS = 2

    EMAIL = 3

    SOCIAL = 4

    SEARCH = 5

    PPC = 6

    DIRECT = 7

    GUARDIAN_APP_IOS = 8

    GUARDIAN_APP_ANDROID = 9

}

/**
* Represents monetary contribution made by a reader via this page.
**/
struct Acquisition {

    /**
    * Product type
    **/
    1: required product.Product product

    /**
    *  If the payment is a one off or reccuring
    **/
    2: required PaymentFrequency paymentFrequency

    /**
    *  Currency code (ISO 4217 3-character, upper-case, eg USD, GBP) 
    **/ 
    3: required string currency
    
    /** 
    *  Contributed amount in units of the currency received 
    **/ 
    4: required double amount

    /**
    * The payment provider the user paid with
    **/
    6: optional PaymentProvider paymentProvider

    /**
    * The campaign code of the campaign that the user came from
    **/
    7: optional set<string> campaignCode

    /**
    * The ab tests the user was in on dotcom
    **/
    8: optional abtest.AbTestInfo abTests

    /**
    *  ISO-3166-aplha-2 country code representing where the user is from. It is either the country of the card used, or the country of the contributor s address
    **/
    9: optional string countryCode

    /**
    *  Page view id of the page the reader was on that lead them through to the contribution page e.g. by clicking on the Contribute Now button in the Epic component
    **/
    10: optional string referrerPageViewId

    /**
    *  Url of the page the reader was on that lead them through to the contribution page e.g. by clicking on the Contribute Now button in the Epic component
    **/
    11: optional string referrerUrl

    /**
    * An ID that can be used to distinguish different instances of a given component type
    **/
    12: optional string componentId

    /**
    * The type of component that the acquisition came from
    **/
    14: optional componentevent.ComponentType componentTypeV2

    /**
    * The source of the acquisition
    **/
    15: optional AcquisitionSource source

    /**
    * Extra parameters only applicable to print products
    **/
    16: optional printoptions.PrintOptions printOptions

    /**
    * Platform on which the acquisition occurred e.g. Contributions, S&C
    **/
    19: optional platform.Platform platform

    /**
    * Length of the discount in months
    **/
    20: optional i16 discountLengthInMonths

    /**
    * Amount of discount, as a percentage
    **/
    21: optional double discountPercentage

    /**
    * Promo code for the acquisition. Only applicable to subscriptions.
    **/
    22: optional string promoCode

    /**
    * Any additional labels.
    * In particular, extra information that is needed to calculate Annualised Value
    * for this Acquisition (e.g. promotions like Guardian Weekly Six For Six) can go here.
    **/
    23: optional set<string> labels

    /**
    * The identity id of a user
    **/
    24: optional string identityId

    /**
    * Query string parameters associated with an acquisition
    **/
    25: optional set<queryparameter.QueryParameter> queryParameters
}
