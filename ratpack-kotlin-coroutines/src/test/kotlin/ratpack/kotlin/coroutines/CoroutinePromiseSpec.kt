package ratpack.kotlin.coroutines

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import ratpack.exec.Promise
import ratpack.exec.util.ParallelBatch
import ratpack.kotlin.test.embed.ratpack
import ratpack.test.embed.EmbeddedApp
import kotlin.concurrent.thread
import kotlin.system.measureTimeMillis

@kotlinx.coroutines.ExperimentalCoroutinesApi
class CoroutinePromiseSpec : Spek({
  given("An embedded app") {
    lateinit var embeddedApp: EmbeddedApp
    beforeEachTest {
      embeddedApp = ratpack {
        handlers {
          get("test") {
            async {
              val p1 = Promise.async<String> { d ->
                thread {
                  Thread.sleep(1000)
                  d.success("p1")
                }
              }
              val p2 = Promise.async<String> { d ->
                thread {
                  Thread.sleep(1000)
                  d.success("p2")
                }
              }
              render(zip(p1, p2) { r1, r2 -> "$r1:$r2" })
            }
          }
          get("test2") {
            val p1 = Promise.async<String> { d ->
              thread {
                Thread.sleep(1000)
                d.success("p1")
              }
            }
            val p2 = Promise.async<String> { d ->
              thread {
                Thread.sleep(1000)
                d.success("p2")
              }
            }
            ParallelBatch.of(p1, p2).yield().then {
              render(it.joinToString(":"))
            }
          }
        }
      }
    }
    afterEachTest {
      embeddedApp.close()
    }
    test("parallel promises with coroutines - sanity") {
      val response = embeddedApp.httpClient.get("test")
      check(response.body.text == "p1:p2")
    }
    test("parallel promises with coroutines - load") {
      runBlocking {
        val time = measureTimeMillis {
          (1..100).map {
            GlobalScope.async {
              embeddedApp.httpClient.get("test")
            }
          }.awaitAll()
        }
        println(time)
      }
    }
  }
})

