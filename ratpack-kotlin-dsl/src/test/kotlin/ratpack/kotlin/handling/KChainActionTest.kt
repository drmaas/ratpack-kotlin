package ratpack.kotlin.handling

import io.kotlintest.matchers.shouldEqual
import io.kotlintest.specs.BehaviorSpec
import ratpack.kotlin.test.embed.ratpack
import ratpack.kotlin.test.testHttpClient

class KChainActionTest : BehaviorSpec() {
  init {
    // ratpack chain dsl test
    given("a ratpack server") {
      val app = ratpack {
        bindings {
          bindInstance(SampleKChainAction())
        }
        handlers {
          prefix("v1",SampleKChainAction::class.java)
        }
      }
      `when`("a request is made") {
        val client = testHttpClient(app)
        val r = client.get("v1/test")
        then("it works") {
          r.statusCode shouldEqual 200
          r.body.text shouldEqual "hello"
          app.close()
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
