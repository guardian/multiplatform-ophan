namespace * ophan.thrift.event
namespace cocoa GLAOphanThriftEvent

/**
* Represents an interaction on the web or apps
**/
struct Interaction {

  /**
  * Key for the interaction.
  **/
  1: required string component

  /**
  * Value associated with the interaction
  **/
  2: optional string value

  /**
  * The id of the atom that the interaction has come from
  **/
  3: optional string atomId
}