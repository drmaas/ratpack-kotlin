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
          all(chain(SampleKChainAction::class.java))
          prefix("v1", SampleKChainAction::class.java)
        }
      }
      `when`("a request to get is made") {
        val client = testHttpClient(app)
        val r = client.get("v1/test")
        then("it works") {
          r.statusCode shouldEqual 200
          r.body.text shouldEqual "hello"
          app.close()
        }
      }
      `when`("a request to get is made without prefix") {
        val client = testHttpClient(app)
        val r = client.get("test")
        then("it works") {
          r.statusCode shouldEqual 200
          r.body.text shouldEqual "hello"
          app.close()
        }
      }
      `when`("a request to path is made") {
        val client = testHttpClient(app)
        val r = client.get("path")
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
    path("path") {
      byMethod {
        get {
          byContent {
            json {
              render("hello")
            }
          }
        }
      }
    }
  }
}
