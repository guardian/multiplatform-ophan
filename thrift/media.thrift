namespace * ophan.thrift.event
namespace cocoa GLAOphanThriftEvent

enum MediaType {
  VIDEO = 1
  AUDIO = 2
}


enum MediaEvent {
  /**
  * The media has been requested.
  * Currently, it appears this event is only received for pre-roll videos.
  **/
  REQUEST = 1

  /**
  * The media is ready to play.
  **/
  READY = 2

  /**
  * The media has started playing.
  **/
  PLAY = 3

  /**
  * The media has played a quarter of the way through.
  * Currently, it appears that pre-roll videos do not send this event.
  **/
  PERCENT25 = 4

  /**
  * The media has played half way though.
  * Currently, it appears that pre-roll videos do not send this event.
  **/
  PERCENT50 = 5

  /**
  * The media has played three quarters of the way though.
  * Currently, it appears that pre-roll videos do not send this event.
  **/
  PERCENT75 = 6

  /**
  * The media has played to the end.
  * NB: "END" is a reserved word in thrift apparently
  **/
  THE_END = 7
}


/**
* Details about media playback progress
* Note: currently only guardian-hosted videos are reported.
**/
struct MediaPlayback {
  /**
  * The id of the media asset, e.g. gu-video-454297906
  * This matches up with the media id within the content api.
  **/
  1: required string mediaId

  /**
  * The media type
  **/
  2: required MediaType mediaType

  /**
  * If true, this event relates to the pre-roll (ad) of this media.
  * If false, this event reated to the core media content.
  **/
  3: required bool preroll

  /**
  * The event type.
  **/
  4: required MediaEvent eventType

}