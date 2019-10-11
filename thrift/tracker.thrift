namespace * ophan.thrift.tracker
namespace cocoa GLAOphanThriftTracker

include "event.thrift"

// this include is in here so that the generated docs include the native app docs too
include "nativeapp.thrift"


enum SubmissionType {

    /** Initial web request */
    WEB_INITIAL = 1

    /** Additional web request */
    WEB_ADDITIONAL = 2

    /** Native mobile app submission */
    NATIVE_APP = 3
}

/**
* This struct is intended for private consumption inside ophan only.
*
* It represents the raw data collected by the tracker, passed onto The Slab for
* enhancement into ophan events.
**/
struct TrackerSubmission {

    /** The type of this submission
     */
    7: required SubmissionType submissionType

    /** Unique id associated with this submission. Ophan never makes
     * better than at-least-once delivery promises, so you
     * must ensure that processing two events with the same
     * uniqueRawTrackerSubmissionId has no effect
     *
     * Note that for web submissions, this id is likely passed though as the
     * unqiue event id for the events, but as native mobile submissions may represent multiple
     * events, each event contained within this submission must have a diffrentiated id.
     */
    1: required string uniqueSubmissionId

    /** The date time (in millis since epoch UTC) at which this submission
     * was received by the tracker.
     */
    2: required i64 dt

    /**
    * Raw values of all query parameters supplied, uri-decoded.
    * Where paramaters with multiple values, only the first value is supplied.
    **/
    3: required map<string, string> queryParams

    /**
    * Values of all headers, except COOKIE
    **/
    4: required map<string, string> headers

    /**
    * Values of a small, whitelisted, set of cookies.
    **/
    5: required map<string, string> significantCookies

    /**
    * If the request to the tracker was a post, the string body associated with
    * the post. This is typically used only in the case of mobile event submission.
    **/
    6: optional string postData

    /**
    * Detail of a native app submission.
    **/
    12: optional nativeapp.NativeAppSubmission nativeAppSubmission

    /** The unqiue id associated with this browser.
     * Currently this is maintained by setting a cookie for web
     * events, or otherwise determined for native apps.
     */
    10: required event.AssignedId browserId

    /** The unique id associated with this "visit".
     * For web reports, the visit id is a refreshed session
     * cookie that expires after 30 minutes of activity.
     * Mobile apps do not currently set this value.
     */
    11: optional event.AssignedId visitId

}