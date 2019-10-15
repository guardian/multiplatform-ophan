namespace * ophan.thrift.event
namespace cocoa GLAOphanThriftEvent

include "url.thrift"
include "link.thrift"
include "platform.thrift"
include "source.thrift"
include "googlereferral.thrift"

/**
* Information about the referrer - previous page - that the reader navigated to
* this one from.
**/
struct Referrer {
    /**
    * Web: The "raw" value in here is the value of
    * document.referrer as reported by the browser
    * Native app: If this is populated, we will use it.
    * If it is not populated, it will be synthesised using
    * the nativeAppSource field.
    **/
    1: optional url.Url url

    /** the component that was clicked on for this referral,
     *  if the previous page was served by the guardian
     */
    4: optional string component

    /**
    * The link name associated with the element clicked.
    */
    10: optional link.LinkName linkName

    /** the platform of the referrer,
     *  if the previous page was served by the guardian
     */
    5: optional platform.Platform platform

    /** the viewId of the referrer,
     *  if the previous page was served by the guardian
     */
    6: optional string viewId

    /** if this was from a guardian email, what email it was */
    7: optional string email

    /** If this referral was from a native app, the source of the referral */
    8: optional source.Source nativeAppSource


    /** If this referral was from google, and we have additional data on the query string,
    * the values we got.
    **/
    9: optional googlereferral.GoogleReferral google

    /*
     * The id of the tag that the user has followed. Usually for native app only.
     * Populated for push notifications only.
     */
    11: optional string tagIdFollowed
}