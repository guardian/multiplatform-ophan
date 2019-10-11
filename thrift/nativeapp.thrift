namespace * ophan.thrift.nativeapp
namespace cocoa GLAOphanThriftNativeApp

include "ads.thrift"
include "benchmark.thrift"
include "abtest.thrift"
include "interaction.thrift"
include "referrer.thrift"
include "source.thrift"
include "media.thrift"
include "subscription.thrift"
include "url.thrift"
include "componentevent.thrift"
include "acquisition.thrift"
include "device.thrift"
include "platform.thrift"

enum EventType {
   /**
    * When a user views a 'page'.
    **/
   VIEW

   /**
   * An ad has been loaded
   **/
   AD_LOAD

    /**
     * The event contains performance benchmark data.
     **/
    PERFORMANCE

    /**
     * The event contains profiling data for network based operations.
     **/
    NETWORK

    /**
    * The event contains information about an in-page interaction
    **/
    INTERACTION

    /**
    * The event contains information about the AB tests.
    **/
    AB_TEST

    /**
    * The event contains information about components.
    **/
    COMPONENT_EVENT

    /**
    * The event contains information about acquisitions.
    **/
    ACQUISITION
}

struct ScrollDepth {
  /**
  * The maximum percentage of the page seen by the user
  **/
  1: required i32 maxExtent

  /**
  * Total number of containers that were displayed on the page
  * Only applicable for fronts
  **/
  2: optional i32 numberOfContainers

  /**
  * Number of containers acctually viewed by the user
  * Only applicable for fronts
  **/
  3: optional i32 numberOfContainersViewed
}


/**
 * E.g. a 'page view' see EventType.
 **/
struct Event {

   /**
    * The type of this event
    */
    3: optional EventType eventType = EventType.VIEW

   /**
     * Unique id associated with this specific event.
     *
     * You must make sure this is globally unqiue: ophan will only process one event per eventId.
     */
    1: required string eventId

   /**
     * The id of this page view. Defaults to the same as event Id which is fine for events of type View.
     * However, AD_LOAD events must set this to be the same as the viewId of the of the page view on which
     * this ad is shown.
     */
   9: optional string viewId

   /**
     * This is for reporting offline events.
     *
     * The number of milliseconds ago that the event occurred. (We deliberately don't
     * use an absolute timestamp to avoid issues with clocks on mobile devices being incorrect)
     *
     * This number should be zero or a positive number, never negative (that would mean in the future!).
     *
     * If an event has just happened, set this value to 0.
     */
   22: optional i64 ageMsLong

   /**
     * DEPRECATED - use ageMsLong instead
     *
     * The i32 type is a 32-bit signed integer, so can only represent up to 2^31 milliseconds (24.9 days) of age
     * without overflow - if a device is offline longer than a month we would get some weird 'future' events.
     *
     */
   2: optional i32 ageMs = 0

   /**
    * Represents the page that has been displayed.
    * For content pages, this should the exact content api path with a "/" prefix.
    * For other pages, this should be the path of the corresponding web page on theguardian.com.
    *
    * This is mandatory if eventType is VIEW.
    */
    4: optional string path

   /**
    * DEPRECATED - use referrer
    * The referring path, i.e. the path representing a page displyed on the app on
    * which the user clicked a link to arrive at this page.
    */
    5: optional string previousPath

   /**
    * DEPRECATED - use referrer
    * The means by which the user arrived at this page.
    */
    6: optional source.Source referringSource

   /**
    * An id which we can link back to Pushy.
    */
    7: optional string pushNotificationId

    /**
    * Details about a rendered ad.
    * Only applicable if eventType is AD_LOAD.
    **/
    8: optional ads.RenderedAd adLoad

    /**
    * Contains benchmark data.
    * Only applicable if eventType is PERFORMANCE.
    */
    10: optional benchmark.BenchmarkData benchmark

    /**
    * Contains performance data for network based operations.
    * Only applicable if eventType is NETWORK.
    */
    11: optional benchmark.NetworkOperationData networkOperation

    /**
    * Attention time spent on this page view in milliseconds
    * Only applicable if eventType is INTERACTION.
    **/
    12: optional i64 attentionMs

    /**
    * Details of how far through a page a user has scrolled
    * Only applicable if eventType is INTERACTION.
    **/
    13: optional ScrollDepth scrollDepth

    /**
    * If populated, this event includes data about media playback
    **/
    14: optional media.MediaPlayback media

    /**
    * If populated, this event includes data about ab tests that the user was a member of
    **/
    15: optional abtest.AbTestInfo ab

    /**
    * If populated, this event includes data about in-app interactions.
    **/
    16: optional interaction.Interaction interaction

    /**
    * If populated, contains information about the referrer/previous page in the apps.
    **/
    17: optional referrer.Referrer referrer

    /**
    * Represents the url of the page that has been displayed.
    * Only applicable if eventType is VIEW.
    **/
    18: optional url.Url url
    
    /**
    * If populated, contains information about the components present on the view.
    * Only applicable if eventType is VIEW.
    **/
    19: optional list<string> renderedComponents

    /**
    * If populated, this event includes data about components
    **/
    20: optional componentevent.ComponentEvent componentEvent

    /**
    * If populated, this event includes data about an acquisition
    **/
    21: optional acquisition.Acquisition acquisition
}

/**
 * A specific version of the app exisits for each edition in the content api.
 * For example, US visitors get the US version of our app.
 */
enum Edition {
    UK
    US
    AU
    International
}

/**
 * Details about this running application
 */
struct App {

   /**
    * The version of the app.
    */
    1: optional string version

   /**
    * The device family.
    */
    2: optional string family

   /**
    * The device's os.
    */
    3: optional string os

   /**
    * The edition of the app.
    */
    4: optional Edition edition

    /* the Guardian platform upon which this app is based. This should
     * essentially be (at the time of writing) either the Guardian
     * Live app, or the Daily Edition / Editions app. This is not
     * related to the `edition` just above this one, which relates to
     * the subset content that is being shown within the Native app
     * (see the comment on the Edition enum).
     */

    5: optional platform.Platform platform
}

struct Device {

    1: optional string name

    2: optional string manufacturer

    3: optional device.DeviceClass deviceClass
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
**/
struct NativeAppSubmission {

   /**
    * App specific information.
    */
    2: required App app

   /**
    * Device specific information.
    */
    3: optional Device device

   /**
    * Equivalent to a web cookie. A way of identifying unique devices.
    */
    4: required string deviceId

   /**
    * The user’s guardian user id if they are logged in.
    */
    5: optional string userId

    /**
    * The krux identifer for this user.
    */
    8: optional string kruxId;

   /**
    * What type of subscription does this user have?
    */
    6: optional subscription.SubscriptionType subscriptionId

    /**
    * The interaction events contained within this submission.
    **/
    7: required list<Event> events

    /**
    * If this user is a member, what tier are they currently a part of?
    **/
    9: optional subscription.MembershipTier membershipTier
}
