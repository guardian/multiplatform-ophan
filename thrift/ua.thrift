namespace * ophan.thrift.ua
namespace cocoa GLAOphanThriftUA

include "nativeapp.thrift"

struct NameAndVersion {
    1: required string name
    2: optional string version
}


enum UserAgentType {
    /**
     * A web browser, especially on a desktop, notebook or workstation
     **/
    BROWSER = 1

    /** An email client, email reader  */
    EMAIL_CLIENT = 100

    /**
     * A <b>news aggregator</b>, also termed a <b>feed aggregator<b>, <b>feed reader</b>, <b>news reader</b>, <b>RSS
     * reader</b> or simply <b>aggregator</b>
     */
    FEED_READER = 101

    /**
     * A library is a collection of resources used to develop software.
     */
    LIBRARY = 102

    /**
     * Media player, also called multimedia player, is a term typically used to describe computer software for playing
     * back multimedia files.
     */
    MEDIAPLAYER = 103

    /**
     * A mobile browser, also called a microbrowser, minibrowser, or wireless internet browser (WIB), is a web browser
     * designed for use on a mobile device.
     */
    MOBILE_BROWSER = 2

    /**
     * Offline browser which may download completely or partially a website to a hard drive
     */
    OFFLINE_BROWSER = 104

    /**
     * A software that doesn't match the other types
     */
    OTHER = 3

    /**
     * Bots, such as Web crawlers
     */
    ROBOT = 4

    /**
     * An unknown user agent type
     */
    UNKNOWN = 5

    /**
     * A software to hide the real user agent information
     */
    USERAGENT_ANONYMIZER = 105

    /**
     * A software or service that validates parts of a website or webservice, such as CSS, HTML, JSON, XML
     */
    VALIDATOR = 106

    /**
     * A WAP browser is a web browser for mobile devices such as mobile phones that uses the Wireless Application
     * Protocol (WAP). WAP is a technical standard for accessing information over a mobile wireless network.
     */
    WAP_BROWSER = 107

    /**
     * One of the guardian native apps
     **/
    GUARDIAN_NATIVE_APP = 6

}


enum DeviceType {
    /**
     * A game console is an interactive computer that produces a video display signal which can be used with a
     * display device (a television, monitor, etc.) to display a video game. The term "game console" is used to
     * distinguish a machine designed for people to buy and use primarily for playing video games on a TV in
     * contrast to arcade machines, handheld game consoles, or home computers.
     */
    GAME_CONSOLE = 1

    /**
     * A personal digital assistant (PDA), also known as a palmtop computer, or personal data assistant, is a mobile
     * device that functions as a personal information manager. PDAs are largely considered obsolete with the
     * widespread adoption of smartphones.
     */
    PDA = 3

    /**
     * A personal computer (PC) is a general-purpose computer, whose size, capabilities, and original sale price
     * makes it useful for individuals, and which is intended to be operated directly by an end-user with no
     * intervening computer operator.
     */
    PERSONAL_COMPUTER = 4

    /**
     * A smart TV, sometimes referred to as connected TV or hybrid TV
     */
    SMART_TV = 5

    /**
     * A smartphone is a mobile phone built on a mobile operating system, with more advanced computing capability
     * and connectivity than a feature phone
     */
    SMARTPHONE = 6

    /**
     * A tablet computer, or simply tablet, is a mobile computer with display, circuitry and battery in a single
     * unit. Tablets are often equipped with sensors, including cameras, microphone, accelerometer and touchscreen,
     * with finger or stylus gestures replacing computer mouse and keyboard.
     */
    TABLET = 7

    /**
     * An unknown device category
     */
    UNKNOWN = 8

    /**
     * Wearable computers, also known as body-borne computers are miniature electronic devices that are worn by the
     * bearer under, with or on top of clothing.
     */
    WEARABLE_COMPUTER = 9

    /**
    * A device that doesn't match the other categories
    **/
    OTHER = 10

    /**
    * The Guardian android native app
    **/
    GUARDIAN_ANDROID_NATIVE_APP = 11

    /**
    * The Guardian iOS native app
    **/
    GUARDIAN_IOS_NATIVE_APP = 12

    /**
    * The Guardian iOS native app
    **/
    GUARDIAN_WINDOWS_APP = 13

}


/** Details about the user agent (browser or native application) that made this request */
struct UserAgent {

    /** The raw user agent string supplied
     *  Note that for guardian native app events this is likely misleading, as it
     *  often represents the library that the app used to make the request
     */
    1: required string userAgentString

    /** The classification of this user agent */
    2: optional UserAgentType type


    /** The operating system this request was made from */
    3: optional NameAndVersion os

    /** The family name and version of the user agent
     *  e.g. Firefox 13.0 or iOS Core 3.1.1
     */
    4: optional NameAndVersion family

    /** Device class of this user agent */
    5: optional DeviceType device

    /** Full device model and manufacturer
    * Only populated for native apps, where this data is available.
    **/
    6: optional nativeapp.Device model
}



