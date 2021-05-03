package ratpack.kotlin.rx2

import io.kotlintest.shouldBe
import io.kotlintest.specs.BehaviorSpec
import io.reactivex.Observable
import io.reactivex.functions.Consumer
import ratpack.exec.Promise
import ratpack.kotlin.test.embed.ratpack
import ratpack.kotlin.test.testHttpClient

class RxRatpackKotlinTest : BehaviorSpec() {

  init {
    // test all with a closure
    given("test promise to observable") {
      val app = ratpack {
        bindings {
          initialize()
        }
        handlers {
          all {
            promise(listOf("hello", "world")).observe().promiseAll().then {
              render(it.joinToString(" "))
            }
          }
        }
      }
      `when`("a request is made to an all closure") {
        val client = testHttpClient(app)
        val r = client.get("")
        then("it works") {
          r.statusCode shouldBe 200
          r.body.text shouldBe "hello world"
        }
      }
      app.close()
    }
    given("test promise to single") {
      val app = ratpack {
        bindings {
          initialize()
        }
        handlers {
          all {
            promise("hello").single().subscribe(Consumer {
              render(it)
            })
          }
        }
      }
      `when`("a request is made to an all closure") {
        val client = testHttpClient(app)
        val r = client.get("")
        then("it works") {
          r.statusCode shouldBe 200
          r.body.text shouldBe "hello"
        }
      }
      app.close()
    }
    given("test observable to promise") {
      val app = ratpack {
        bindings {
          initialize()
        }
        handlers {
          all {
            observable(listOf("hello", "world")).promiseAll().then {
              render(it.joinToString(" "))
            }
          }
        }
      }
      `when`("a request is made to an all closure") {
        val client = testHttpClient(app)
        val r = client.get("")
        then("it works") {
          r.statusCode shouldBe 200
          r.body.text shouldBe "hello world"
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

fun promise(list: List<String>): Promise<List<String>> {
  return Promise.async {
    it.success(list)
  }
}

fun observable(list: List<String>): Observable<String> {
  return Observable.create { e ->
    for (s in list) {
      e.onNext(s)
    }
    e.onComplete()
  }
}
