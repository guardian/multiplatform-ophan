namespace * ophan.thrift.event
namespace cocoa GLAOphanThriftEvent

/**
* Represents the rendering of an ad on this page.
**/
struct RenderedAd {
  /**
  * The name of the ad slot in which this ad was rendered.
  **/
  1: required string slot

  /**
  * The DFP line item id populated in this slot.
  **/
  2: optional i64 lineItemId

  /**
  * The DFP creative id populated in this slot.
  **/
  3: optional i64 creativeId

  /**
  * The approximate amount of time (in milliseconds) that this
  * ad took to render.
  **/
  4: optional i64 timeToRenderEndedMs

  /**
   * The approximate amount of time (in milliseconds) that elapsed
   * before this ad was requested.
   */
  5: optional i64 timeToAdRequestMs

  /**
   * The approximate amount of time (in milliseconds) between requesting
   * and receiving this ad.
   */
  6: optional i64 adRetrievalTimeMs

  /**
   * The approximate amount of time (in milliseconds) between receiving
   * and rendering this ad.
   */
  7: optional i64 adRenderTimeMs
}

