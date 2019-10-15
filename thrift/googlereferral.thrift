namespace * ophan.thrift.event
namespace cocoa GLAOphanThriftEvent

/**
* Where the referrer was google and they've provided additional information
* on the query string, here is
* that additional information.
**/
struct GoogleReferral {

  /**
  * The query terms requested by the user
  **/
  1: optional string q

  /**
  * The rank we were list at within the source, as indicated by the "cd" query
  * parameter.
  **/
  2: optional i32 rank

  /**
  * The type of referral this was. Currently this is just a string, and is likely to
  * change as we overhaul our google ved parsing
  **/
  3: optional string source
}