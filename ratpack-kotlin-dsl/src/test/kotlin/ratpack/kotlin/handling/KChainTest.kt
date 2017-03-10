package ratpack.kotlin.handling

import io.kotlintest.specs.BehaviorSpec
import ratpack.handling.Context
import ratpack.handling.Handler
import ratpack.kotlin.test.RatpackKotlinApplicationUnderTest
import ratpack.kotlin.test.ratpack
import ratpack.test.http.TestHttpClient

class KChainTest: BehaviorSpec() {

  var app: RatpackKotlinApplicationUnderTest? = null

  init {
    // test all with a closure
    given("a ratpack server") {
      app = ratpack {
        serverConfig {
          port(8080)
        }
        handlers {
          all {
            render("hello")
          }
        }
      }
      `when`("a request is made to an all closure") {
        val client = TestHttpClient.testHttpClient(app)
        val r = client.get("")
        then("it works") {
          r.statusCode shouldEqual 200
          r.body.text shouldEqual "hello"
        }
      }
      app?.stop()
    }

    // test all with a handler instance
    given("a ratpack server") {
      app = ratpack {
        serverConfig {
          port(8080)
        }
        handlers {
          all(AllHandler())
        }
      }
      `when`("a request is made to an all handler") {
        val client = TestHttpClient.testHttpClient(app)
        val r = client.get("")
        then("it works") {
          r.statusCode shouldEqual 200
          r.body.text shouldEqual "hello"
        }
      }
      app?.stop()
    }

    // test all with a handler class
    given("a ratpack server") {
      app = ratpack {
        serverConfig {
          port(8080)
        }
        bindings {
          bindInstance(AllHandler())
        }
        handlers {
          all(AllHandler::class.java)
        }
      }
      `when`("a request is made to an all handler") {
        val client = TestHttpClient.testHttpClient(app)
        val r = client.get("")
        then("it works") {
          r.statusCode shouldEqual 200
          r.body.text shouldEqual "hello"
        }
      }
      app?.stop()
    }

  }
}

class AllHandler: Handler {
  override fun handle(ctx: Context) {
    ctx.render("hello")
  }
}
