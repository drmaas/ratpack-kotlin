package ratpack.kotlin.handling

import io.kotlintest.specs.BehaviorSpec
import ratpack.kotlin.test.ratpack
import ratpack.test.http.TestHttpClient

class KChainActionTest : BehaviorSpec() {
  init {
    // ratpack chain dsl test
    given("a ratpack server") {
      val app = ratpack {
        serverConfig {
          port(8080)
        }
        bindings {
          bindInstance(SampleKChainAction())
        }
        handlers {
          prefix("v1",SampleKChainAction::class.java)
        }
      }
      `when`("a request is made") {
        val client = TestHttpClient.testHttpClient(app)
        val r = client.get("v1/test")
        then("it works") {
          r.statusCode shouldEqual 200
          r.body.text shouldEqual "hello"
          app.stop()
        }
      }
    }
  }
}

class SampleKChainAction : KChainAction() {
  override fun execute() {
    get("test") {
      render("hello")
    }
  }
}
