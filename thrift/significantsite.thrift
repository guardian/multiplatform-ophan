namespace * ophan.thrift.event
namespace cocoa GLAOphanThriftEvent

/**
* Indicates sites that we really care about, factoring out the variation
* on domain that they sometimes contain.
**/
enum SignificantSite {
  /**
  * The Guardian website, including when viewed on native apps and
  * for pages served on non www. subdomains such as membership.theguardian.com
  * and careers.theguardian.com
  **/
  GUARDIAN = 0

  /** An email sent by the Guardian, either one of our scheduled emails
   * or by a user clicking "email this"  **/
  GUARDIAN_EMAIL = 1

  /**
  * An app push alert sent by the Guardian
  **/
  GUARDIAN_PUSH = 15

  /**
   * Any google website, regardless of subdomain and country domain
   * i.e. includes news.google.co.uk and www.google.ca
   * also includes google plus?
   **/
  GOOGLE = 2

  /**
  * Twitter, including any where the source was a result of going via
  * the t.co redirector
  **/
  TWITTER = 3

  /**
  * Facebook, including when we've inferrer that the referrer was facebook
  * because we were access inside a facebook native app.
  **/
  FACEBOOK = 4

  REDDIT = 5

  DRUDGE_REPORT = 6

  /** Outbrain, both paid and non-paid **/
  OUTBRAIN = 7

  TUMBLR = 8

  PINTEREST = 9

  DIGG = 10

  STUMBLEUPON = 11

  FLIPBOARD = 12

  LINKEDIN = 13

  BING = 14

  SPOTLIGHT = 16

  WE_CHAT = 17

  WHATS_APP = 18

  APPLE_NEWS = 19

  IN_SHORTS = 20

  UPDAY = 21

  SMART_NEWS = 22

}