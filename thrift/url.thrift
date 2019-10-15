namespace * ophan.thrift.event
namespace cocoa GLAOphanThriftEvent

include "significantsite.thrift"

/**
* Represents a url either of a page served or a referrer
**/
struct Url {
    /** The full raw URL as provided to ophan, potentially including
     * query string and fragment identifier
     *
     *  Be careful about using the value of this field: almost certainly
     *  you want to use the combination of domain and path instead, which are cleared of
     *  non-significant variation.
     */
  1: required string raw

  /** the host of this url with no parsing or normalisation performed.
   *
   *  Be careful about using the value of this field: almost certainly
   *  you want to use the combination of domain and path instead, which are cleared of
   *  non-significant variation.
   **/
  2: required string host

  /**
   * the domain of this url.
   *
   * This is the host stripped down to one level below the top level domain.
   *
   * e.g.
   * www.theguardian.com => theguardian.com
   * news.google.co.uk => google.co.uk
   * membership.theguardian.com => theguardian.com
   **/
  6: required string domain

  /**
  * The path served on the given host, without query strings or fragment identifiers.
  *
  * For guardian urls, processing is performed on the path -
  * especially for the native mobile apps - to try to make the url
  * match up with those on www.theguardian.com
  *
  * Path will always start with a /.
  **/
  3: required string path

  /**
  * Indicates sites that we really care about, factoring out the variation
  * on domain that they sometimes contain.
  *
  * Note that were this url represents a page on a site that we don't consider significant,
  * this value will be unpopulated. You should probably use domain to aggregate in that case.
  **/
  5: optional significantsite.SignificantSite site

  /** Indicates whether this url was synthesised in some way by ophan */
  11: optional bool synthesised = false
}