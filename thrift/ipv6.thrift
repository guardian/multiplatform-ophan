namespace * ophan.thrift.event
namespace cocoa GLAOphanThriftEvent

enum IpConnectivityResult {
  /**
  * Connection test was a result and was made over IPv6
  **/
  SUCCESS_IPV6 = 1

  /**
  * Connection test was a result and was made over IPv4
  **/
  SUCCESS_IPV4 = 2

  /**
  * Connection test was a result and it was not possible to determine which
   * protocol was used
  **/
  SUCCESS_UNKNOWN = 3

  /**
  * Connection test experienced an error (XHR.onerror)
  **/
  NET_ERROR = 4

  /**
  * Connection test experienced an error (Non-200 response)
  **/
  LOAD_ERROR = 5

  /**
  * Connection test timed out on the client
  **/
  TIMEOUT = 6
}

struct IpConnectivity {
  /**
  * The IP connectivity result from a server with only A records
  **/
  1: required IpConnectivityResult ipv4

  /**
  * The IP connectivity result from a server with both A and AAAA records
  **/
  2: required IpConnectivityResult dual

  /**
  * The IP connectivity result from a server with only AAAA records
  **/
  3: required IpConnectivityResult ipv6
}