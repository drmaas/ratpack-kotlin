package ratpack.kotlin.rx

import io.kotlintest.matchers.shouldEqual
import io.kotlintest.specs.BehaviorSpec
import ratpack.exec.Promise
import ratpack.kotlin.test.embed.ratpack
import ratpack.kotlin.test.testHttpClient
import rx.Observable

class RxRatpackKotlinTest : BehaviorSpec() {

  init {
    // test all with a closure
    given("test promise to observable") {
      val app = ratpack {
        serverConfig {
          port(8089)
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
        val client = testHttpClient(app)
        val r = client.get("")
        then("it works") {
          r.statusCode shouldEqual 200
          r.body.text shouldEqual "hello"
        }
      }
      app.close()
    }
    given("test observable to promise") {
      val app = ratpack {
        serverConfig {
          port(8089)
        }
        bindings {
          initialize()
        }
        handlers {
          all {
            observable("hello").promiseSingle().then {
              render(it)
            }
          }
        }
      }
      `when`("a request is made to an all closure") {
        val client = testHttpClient(app)
        val r = client.get("")
        then("it works") {
          r.statusCode shouldEqual 200
          r.body.text shouldEqual "hello"
        }
      }
      app.close()
    }
  }
}

fun promise(s: String): Promise<String> {
  return Promise.async {
    it.success(s)
  }
}

fun observable(string: String): Observable<String> {
  return Observable.create { s ->
    s.onNext(string)
    s.onCompleted()
  }
}
