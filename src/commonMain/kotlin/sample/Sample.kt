package sample

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.response.HttpResponse
import io.ktor.client.response.readText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.io.core.use
import kotlin.coroutines.CoroutineContext

expect class Sample() {
    fun checkMe(): Int
}

expect object Platform {
    fun name(): String
}

fun hello(): String = "Hello from ${Platform.name()}"

fun coroutineTest(context: CoroutineContext) {
    println("entering coroutineTest...")
    CoroutineScope(context).launch {
        println("running in coroutine scope")
        //println("starting 5 second wait...")
        //delay(5000)
        println("making a HTTP request...")
        val response = HttpClient().use { client ->
            client.get<String>("https://mobile.guardianapis.com/uk/navigation")
        }
        println("got a response")
        //println("response code was ${response.status}")
        println("response body was ${response.take(200)}...")
        println("exiting coroutine scope")
    }
    println("coroutineTest is returning")
}
