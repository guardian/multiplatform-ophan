namespace * ophan.thrift.benchmark
namespace cocoa GLAOphanThriftBenchmark

enum RequestType {
	/**
	* Fetch the home and all its groups including personalised groups.
	*/
	HomeFrontAndGroups
	/**
	* Fetch a front and associated groups.
	*/
	FrontAndGroups
	/**
	* Fetch a single group
	*/
	Group
	/**
	* Fetch a list.
	*/
	List
	/**
	* Fetch a single item.
	*/
	Item
	/**
	* Perform a search.
	*/
	Search
}

enum ConnectionType {
	/**
	 * The request was performed over Wifi.
	 */
	Wifi
	/**
	 * The request was performed over a cellular connection.
	 */
	WWAN
}

struct NetworkOperationData {
	/**
	* The request type. This can be a single HTTP call or a succession of requests.
	*/
	1: required RequestType requestType

	/**
	* The time to perform the operation, from the beginning of the request to processing the data.
	*/
	2: required i64 measuredTimeMs

	/**
	* The type of connection used for performing the request.
	*/
	3: optional ConnectionType connectionType

	/**
	* True if the request is considered successful.
	*/
	4: required bool success = true
}

enum BenchmarkType {
	/**
	* Time to display an article from initial tap to DOM being ready.
	*/
	TapToArticleDisplay
	/**
	* Time from app launch to home front front being displayed.
	*/
	LaunchTime
}

struct BenchmarkData {

	/**
	 * An ID that can be used to identify a type benchmark.
	 */
	1: required BenchmarkType type

	/**
	 * The amount of time (in milliseconds) measured by this benchmark.
	 */
	2: required i64 measuredTimeMs

}
