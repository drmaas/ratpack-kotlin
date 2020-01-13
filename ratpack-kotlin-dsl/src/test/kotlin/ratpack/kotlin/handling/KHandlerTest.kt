package ratpack.kotlin.handling

import io.kotlintest.matchers.shouldEqual
import io.kotlintest.specs.BehaviorSpec
import ratpack.kotlin.test.testHttpClient

class KHandlerTest: BehaviorSpec() {
  init {
    // test handler with a class
    given("a ratpack server") {
      val app = ratpack.kotlin.test.embed.ratpack {
        bindings {
          add(MyKHandler())
        }
        handlers {
          get<MyKHandler>("class")
          get("instance", MyKHandler2())
        }
      }
      `when`("a request is made to a class handler") {
        val client = testHttpClient(app)
        val r = client.get("class")
        then("it works") {
          r.statusCode shouldEqual 200
          r.body.text shouldEqual "hello"
          app.close()
        }
      }
      `when`("a request is made to an instance handler") {
        val client = testHttpClient(app)
        val r = client.get("instance")
        then("it works") {
          r.statusCode shouldEqual 200
          r.body.text shouldEqual "hello"
          app.close()
        }
      }
    }
  }
}

class MyKHandler: KHandler {
  override fun handle(ctx: KContext) {
    ctx.render("hello")
  }
}

class MyKHandler2: KHandler {
  override fun handle(ctx: KContext) {
    ctx.render("hello")
  }
}
