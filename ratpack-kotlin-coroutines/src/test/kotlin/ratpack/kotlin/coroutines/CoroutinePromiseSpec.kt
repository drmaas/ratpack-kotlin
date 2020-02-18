package ratpack.kotlin.coroutines

import io.kotlintest.Spec
import io.kotlintest.specs.StringSpec
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import ratpack.exec.Promise
import ratpack.exec.util.ParallelBatch
import ratpack.kotlin.test.embed.ratpack
import ratpack.test.embed.EmbeddedApp
import kotlin.concurrent.thread
import kotlin.system.measureTimeMillis

class CoroutinePromiseSpec : StringSpec() {

  lateinit var embeddedApp: EmbeddedApp

  override fun beforeSpec(spec: Spec) {
    embeddedApp = ratpack {
      handlers {
        get("test") {
          async {
            val p1 = promise {
              delay(1000)
              "p1"
            }
            val p2 = promise {
              delay(1000)
              "p2"
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
        get("test3") {
          async {
            promise {
              "p1"
            }.then {
              render(it)
            }
          }
        }
        get("test4") {
          promise {
            "p1"
          }.then {
            render(it)
          }
        }
      }
    }
  }

  override fun afterSpec(spec: Spec) {
    embeddedApp.close()
  }

  init {
    "parallel promises with coroutines - sanity" {
      val response = embeddedApp.httpClient.get("test")
      check(response.body.text == "p1:p2")
    }
    "parallel promises with coroutines - load" {
      runBlocking {
        val time = measureTimeMillis {
          (1..10).map {
            GlobalScope.async {
              embeddedApp.httpClient.get("test")
            }
          }.awaitAll()
        }
        println(time)
      }
    }
    "parallel promises without coroutines - load" {
      runBlocking {
        val time = measureTimeMillis {
          (1..10).map {
            GlobalScope.async {
              embeddedApp.httpClient.get("test2")
            }
          }.awaitAll()
        }
        println(time)
      }
    }
    "create promise in a coroutine inside global coroutine context" {
      runBlocking {
        val response = embeddedApp.httpClient.get("test3")
        check(response.body.text == "p1")
      }
    }
    "create promise in a coroutine outside global coroutine context" {
      runBlocking {
        val response = embeddedApp.httpClient.get("test4")
        check(response.body.text == "p1")
      }
    }
  }
}

