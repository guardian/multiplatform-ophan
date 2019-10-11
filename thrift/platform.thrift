namespace * ophan.thrift.event
namespace cocoa GLAOphanThriftEvent

/**
* The platform that served this request to the reader.
**/
enum Platform {
    R2 = 0

    NEXT_GEN = 1

    IOS_NATIVE_APP = 2

    ANDROID_NATIVE_APP = 3

    /**
    * Served as a result of embedding a media item on a third party site.
    * Note therefore you should not typically include this as a guardian
    * "page view". **/
    EMBED = 4

    MEMBERSHIP = 5

    FACEBOOK_INSTANT_ARTICLE = 6

    /*  https://www.ampproject.org/ */
    AMP = 7

    WITNESS = 8

    JOBS = 9

    CONTRIBUTION = 10

    YAHOO = 11

    AMAZON_ECHO = 12

    APPLE_NEWS = 13

    WINDOWS_NATIVE_APP = 14

    SCRIBD = 15

    SUPPORT = 16

    SUBSCRIBE = 17

    MANAGE_MY_ACCOUNT = 18

    SMART_NEWS = 19

    /* also called "The Daily Edition" and other names */
    EDITIONS = 20
}
