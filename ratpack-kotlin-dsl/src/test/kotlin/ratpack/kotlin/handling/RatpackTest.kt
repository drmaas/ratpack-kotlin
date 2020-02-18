package ratpack.kotlin.handling

import io.kotlintest.shouldBe
import io.kotlintest.specs.BehaviorSpec
import ratpack.kotlin.test.embed.ratpack
import ratpack.test.http.TestHttpClient.testHttpClient

class RatpackTest : BehaviorSpec() {
  init {
    // ratpack top-level dsl test
    given("a ratpack server") {
      val app = ratpack {
        bindings {
          bindInstance("string")
        }
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
          app.server.registry.get().getAll<String>().toList().size shouldBe 1
          r.statusCode shouldBe 200
          r.body.text shouldBe "hello"
          app.close()
        }
      }
    }
  }
}
