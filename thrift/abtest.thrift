namespace * ophan.thrift.event
namespace cocoa GLAOphanThriftEvent

/**
* Represents the AbTest on the web and apps
**/
struct AbTest {
  /**
  * the test that the user is participating in
  **/
  1: required string name

  /**
  * the variant that they are seeing
  **/
  2: required string variant

  /**
  * whether the test has been completed or not
  **/
  3: optional bool complete

  /**
  * the campaign codes associated with the variant
  **/
  4: optional set<string> campaignCodes
}


struct AbTestInfo {
  /**
  * On the left hand side of the map, .
  * On the right hand side of the map, the variant that they are seeing.
  **/
  1: required set<AbTest> tests

}
