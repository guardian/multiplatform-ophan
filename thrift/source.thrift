namespace * ophan.thrift.nativeapp
namespace cocoa GLAOphanThriftNativeApp

/**
 * The means by which the user arrived at a page.
 *
 * As well as the capitalised versions listed below, these values
 * can be supplied in json as camelCase, e.g. inAppLink
 */
enum Source {

    /**
    * User clicked an link on a front or section page
    **/
    FRONT_OR_SECTION = 0

   /**
    * user clicked a link on a fixtures page
    */
    FIXTURES_PAGE = 1

    /**
    * User swiped across the screen
    **/
    SWIPE = 2

    /**
    * User clicked a link within an article
    **/
    IN_ARTICLE_LINK = 3

   /**
    * Whether the user clicked on a Guardian link (anywhere on the device) and chose to open it using our native app.
    */
    EXTERNAL_LINK = 4

   /**
    * Whether the user clicked on a link in the 'More on this story' component.
    */
    RELATED_ARTICLE_LINK = 5

   /**
    * DEPRECATED - please use one of the other values begining with PUSH_
    * Whether the user came to the page via a push notification.
    * The id can be stored in Event.pushNotificationId.
    */
    PUSH = 6

    /**
    * meaning tbc
    **/
    HANDOFF_WEB = 7

    /**
    * meaning tbc
    **/
    HANDOFF_APP = 8

    /**
    * user clicked a link from a notification centre / home page widget
    **/
    WIDGET = 9

    /**
    * meaning tbc
    **/
    RESUME_MEDIA = 10

    /**
    * user clicked the back button
    **/
    BACK = 11

    /**
    * meaning tbc
    **/
    SEARCH = 12

    /**
    * Wheter the user clicked in iOs Spotlight and open the article in native app
    **/
    SPOTLIGHT = 13

    /**
    * Indicates that the article was displayed automatically by the system during the state restoration process.
    */
    STATE_RESTORATION = 14

    /**
    * Whether the user came to the page via a breaking new push notification.
    */
    PUSH_BREAKING_NEWS = 15

    /**
    * Whether the user came to the page from a push notification that the user received because of following a contributor.
    */
    PUSH_FOLLOW_TAG = 16

    /**
    * Whether the user came to the page from a push notification that user received because of some other events.
    */
    PUSH_OTHER = 17
    
    /**
    * Whether the user came to the page from the Discover feature.
    */
    DISCOVER = 18

    /**
    * Whether the user came to the page from Membership content in their profile.
    */
    MEMBERSHIP = 19

    /**
    * Whether the user came to the page by opening the app from their own home screen.
    */
    HOME_SCREEN = 20

    /**
    * Whether the user came to the page by clicking on the navigation menu of the app
    */
    NAVIGATION = 21

}
