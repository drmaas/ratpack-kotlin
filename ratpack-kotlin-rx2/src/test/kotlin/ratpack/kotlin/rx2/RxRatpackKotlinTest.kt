package ratpack.kotlin.rx2

import io.kotlintest.matchers.shouldEqual
import io.kotlintest.specs.BehaviorSpec
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Observable
import ratpack.exec.Promise
import ratpack.kotlin.rx2.flow
import ratpack.kotlin.rx2.initialize
import ratpack.kotlin.rx2.observe
import ratpack.kotlin.rx2.promiseSingle
import ratpack.kotlin.test.embed.ratpack
import ratpack.kotlin.test.testHttpClient

class RxRatpackKotlinTest : BehaviorSpec() {

  init {
    // test all with a closure
    given("test to observable") {
      val app = ratpack {
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
        val client = testHttpClient(app)
        val r = client.get("")
        then("it works") {
          r.statusCode shouldEqual 200
          r.body.text shouldEqual "hello"
        }
      }
      app.close()
    }
    given("test to flowable") {
      val app = ratpack {
        serverConfig {
          port(8080)
        }
        bindings {
          initialize()
        }
        handlers {
          all {
            promise("hello").flow(BackpressureStrategy.BUFFER).subscribe {
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
          port(8080)
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
    given("test flowable to promise") {
      val app = ratpack {
        serverConfig {
          port(8080)
        }
        bindings {
          initialize()
        }
        handlers {
          all {
            flowable("hello").promiseSingle().then {
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

fun observable(s: String): Observable<String> {
  return Observable.create { e ->
    e.onNext(s)
    e.onComplete()
  }
}

fun flowable(s: String): Flowable<String> {
  return Flowable.create({ e ->
    e.onNext(s)
    e.onComplete()
  }, BackpressureStrategy.BUFFER)
}
