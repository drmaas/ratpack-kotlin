package ratpack.kotlin.handling

import io.kotlintest.matchers.shouldEqual
import io.kotlintest.specs.BehaviorSpec
import ratpack.handling.Context
import ratpack.handling.Handler
import ratpack.kotlin.test.embed.ratpack
import ratpack.kotlin.test.testHttpClient

class KChainTest : BehaviorSpec() {

  init {
    // test all with a closure
    given("a ratpack server") {
      val app = ratpack {
        handlers {
          all {
            render("hello")
          }
        }
      }
      `when`("a request is made to an all closure") {
        val client = testHttpClient(app)
        val r = client.get("")
        then("it works") {
          r.statusCode shouldEqual 200
          r.body.text shouldEqual "hello"
          app.close()
        }
      }
    }

    // test all with a handler instance
    given("a ratpack server") {
      val app = ratpack {
        handlers {
          all(AllHandler())
        }
      }
      `when`("a request is made to an all handler") {
        val client = testHttpClient(app)
        val r = client.get("")
        then("it works") {
          r.statusCode shouldEqual 200
          r.body.text shouldEqual "hello"
          app.close()
        }
      }
    }

    // test all with a handler class
    given("a ratpack server") {
      val app = ratpack {
        bindings {
          bindInstance(AllHandler())
        }
        handlers {
          all<AllHandler>()
        }
      }
      `when`("a request is made to an all handler") {
        val client = testHttpClient(app)
        val r = client.get("")
        then("it works") {
          r.statusCode shouldEqual 200
          r.body.text shouldEqual "hello"
          app.close()
        }
      }
    }

  }
}

class AllHandler : Handler {
  override fun handle(ctx: Context) {
    ctx.render("hello")
  }
}
