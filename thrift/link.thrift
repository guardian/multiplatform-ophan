namespace * ophan.thrift.event
namespace cocoa GLAOphanThriftEvent

/**
* Represents the position and location of a link within the Guardian site.
* We hope to enhance this structure to include a better representation of what the links
* actually mean, but for now we just report exactly what the web site tells us,
* which is a hierarchical list of named items e.g.
*  "more","container-2 | highlights","Front | /uk"
*  "article","news | group-1+ | card-3","container-1 | headlines","Front | /uk"
**/
struct LinkName {

  /* List of link names, most specific first */
  1: optional list<string> raw
}