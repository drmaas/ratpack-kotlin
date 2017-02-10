package ratpack.kotlin.handling

import io.kotlintest.specs.BehaviorSpec
import ratpack.test.http.TestHttpClient.testHttpClient
import ratpack.kotlin.test.ratpack

class RatpackTest : BehaviorSpec() {
  init {
    // ratpack top-level dsl test
    given("a ratpack server") {
      val app = ratpack {
        handlers {
          get("test") {
            render("hello")
          }
        }
      }
      `when`("a request is made") {
        val client = testHttpClient(app)
        val r = client.get("test")
        then("it works") {
          r.body.text shouldEqual "hello"
          app.stop()
        }
      }
    }
  }
}
