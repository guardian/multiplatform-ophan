namespace * ophan.thrift.event
namespace cocoa GLAOphanThriftEvent

include "printproduct.thrift"

/**
* Extra parameters only applicable to print products
**/
struct PrintOptions {
    /**
    * Differentiates between the (many) different types of print product
    **/
    1: required printproduct.PrintProduct product

    /**
    * The deliver country of the print subscription
    **/
    2: required string deliveryCountryCode
}