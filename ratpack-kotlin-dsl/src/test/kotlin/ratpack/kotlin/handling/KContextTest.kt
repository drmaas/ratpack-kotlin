package ratpack.kotlin.handling

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.delay
import ratpack.exec.Promise
import ratpack.kotlin.coroutines.await
import ratpack.kotlin.test.testHttpClient
import java.lang.Thread.sleep
import ratpack.kotlin.test.embed.ratpack

class KContextTest : BehaviorSpec() {

  init {
    // test all with a closure
    given("a ratpack server") {
      val app = ratpack {
        handlers {
          path("test") {
            byMethod {
              get {
                send("hello")
              }
            }
          }
          path("async") {
            byMethod {
              get {
                val text = suspend { await { sleep(100) } ; "hello" }
                send(text())
              }
            }
          }
          path("promise") {
            byMethod {
              get {
                Promise.async<String> { d ->
                  sleep(500)
                  d.success("hello")
                }.then {
                  send(it)
                }
              }
            }
          }
        }
      }
      `when`("a request is made to byContent closure") {
        val client = app.httpClient
        val r = client.get("test")
        then("it works") {
          r.statusCode shouldBe 200
          r.body.text shouldBe "hello"
          app.close()
        }
      }
      `when`("an async request is handled") {
        val client = app.httpClient
        val r = client.get("async")
        then("it works") {
          r.statusCode shouldBe 200
          r.body.text shouldBe "hello"
          app.close()
        }
      }
      `when`("an async promise is handled") {
        val client = app.httpClient
        val r = client.get("promise")
        then("it works") {
          r.statusCode shouldBe 200
          r.body.text shouldBe "hello"
          app.close()
        }
      }
    }
  }
}
