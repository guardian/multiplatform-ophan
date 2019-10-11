namespace * ophan.thrift.event
namespace cocoa GLAOphanThriftEvent

/**
* A query string parameter
**/
struct QueryParameter {

   /**
    * The name of the query string parameter
    **/
    1: required string name

   /**
    * The value of the query string parameter
    **/
    2: required string value
}