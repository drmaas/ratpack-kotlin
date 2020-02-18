package ratpack.kotlin.reactor

import io.kotlintest.shouldBe
import io.kotlintest.specs.BehaviorSpec
import ratpack.exec.Promise
import ratpack.kotlin.test.embed.ratpack
import ratpack.kotlin.test.testHttpClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

class ReactorRatpackKotlinTest : BehaviorSpec() {

  init {
    // test all with a closure
    given("test promise to mono") {
      val app = ratpack {
        serverConfig {
          port(8089)
        }
        bindings {
          initialize()
        }
        handlers {
          all {
            promise("hello").mono().subscribe {
              render(it)
            }
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
    given("test mono to promise") {
      val app = ratpack {
        serverConfig {
          port(8089)
        }
        bindings {
          initialize()
        }
        handlers {
          all {
            mono("hello").promiseSingle().then {
              render(it)
            }
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
  }
}

fun promise(s: String): Promise<String> {
  return Promise.async {
    it.success(s)
  }
}

fun mono(s: String): Mono<String> {
  return Mono.create({ e ->
    e.success(s)
  })
}

fun flux(s: String): Flux<String> {
  return Flux.create({ e ->
    e.next(s)
    e.complete()
  })
}
