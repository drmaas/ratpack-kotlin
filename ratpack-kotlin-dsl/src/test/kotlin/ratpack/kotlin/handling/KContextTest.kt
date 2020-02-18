package ratpack.kotlin.handling

import io.kotlintest.shouldBe
import io.kotlintest.specs.BehaviorSpec
import ratpack.exec.Promise
import ratpack.kotlin.test.testHttpClient
import java.lang.Thread.sleep

class KContextTest : BehaviorSpec() {

  init {
    // test all with a closure
    given("a ratpack server") {
      val app = ratpack.kotlin.test.embed.ratpack {
        handlers {
          path("test") {
            byMethod {
              get {
                render("hello")
              }
            }
          }
          path("async") {
            byMethod {
              get {
                val text = fun(): String { sleep(500); return "hello" }()
                render(text)
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
                  render(it)
                }
              }
            }
          }
        }
      }
      `when`("a request is made to byContent closure") {
        val client = testHttpClient(app)
        val r = client.get("test")
        then("it works") {
          r.statusCode shouldBe 200
          r.body.text shouldBe "hello"
          app.close()
        }
      }
      `when`("an async request is handled") {
        val client = testHttpClient(app)
        val r = client.get("async")
        then("it works") {
          r.statusCode shouldBe 200
          r.body.text shouldBe "hello"
          app.close()
        }
      }
      `when`("an async promise is handled") {
        val client = testHttpClient(app)
        val r = client.get("async")
        then("it works") {
          r.statusCode shouldBe 200
          r.body.text shouldBe "hello"
          app.close()
        }
      }
    }
  }
}
