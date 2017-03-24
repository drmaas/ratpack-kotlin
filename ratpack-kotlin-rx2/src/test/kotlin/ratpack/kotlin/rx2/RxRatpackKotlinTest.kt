package ratpack.kotlin.rx2

import io.kotlintest.specs.BehaviorSpec
import ratpack.exec.Promise
import ratpack.kotlin.test.RatpackKotlinApplicationUnderTest
import ratpack.kotlin.test.ratpack
import ratpack.test.http.TestHttpClient

class RxRatpackKotlinTest : BehaviorSpec() {

  var app: RatpackKotlinApplicationUnderTest? = null

  init {
    // test all with a closure
    given("a ratpack server") {
      app = ratpack {
        serverConfig {
          port(8080)
        }
        bindings {
          initialize()
        }
        handlers {
          all {
            promise("hello").observe().subscribe {
              render(it)
            }
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
  }
}

fun promise(s: String): Promise<String> {
  return Promise.async {
    it.success(s)
  }
}
