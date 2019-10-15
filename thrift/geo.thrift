namespace * ophan.thrift.event
namespace cocoa GLAOphanThriftEvent

struct IpAddress {
    /** The full value of the X-Forwarded-For header supplied.
     * Normally you'll want to ignore this and use the ip field below.
     * This is here just in case there's a bug in our logic of decoding
     * the XFF header, or we can gain more information from the rest of the
     * header.
     */
    1: required string xForwardedForHeader

    /** The ip address of the client */
    2: optional string ip
}


/** A geographical location */
struct GeoPoint {
    /** Latitude */
    1: required double lat
    /** Longitude */
    2: required double lon
}


/**
 *  Where this request was made from, derived by ip address lookup.
 */
struct GeoLocation {

    /** The resolved geolocation of the ip address */
    3: optional GeoPoint geo

    /** The two letter country code of the ip address;
     *  Note that guardian internal traffic (within the Guardian offices)
     *  is assigned a country code of "GNM"
     */
    4: optional string countryCode

    /** Human readable country name */
    5: optional string countryName

    /** Human readable city name */
    6: optional string city

    /** The metro code of the location if the location is in the US. */
    9: optional i32 metroCode

    /** Human readable continent name */
    7: optional string continent

    /** Administrative subdivisions, e.g. "Wales", "Berkshire" or "Alaska"  */
    8: optional list<string> subdivisions

    /** Time zone as specified by http://www.iana.org/time-zones e.g. America/New_York */
    10: optional string timezone
}