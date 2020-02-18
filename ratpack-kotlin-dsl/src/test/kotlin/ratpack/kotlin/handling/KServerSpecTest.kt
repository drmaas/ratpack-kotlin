package ratpack.kotlin.handling

import io.kotlintest.shouldBe
import io.kotlintest.specs.BehaviorSpec
import ratpack.func.Action
import ratpack.kotlin.test.embed.ratpack

class KServerSpecTest : BehaviorSpec() {
  init {
    // ratpack serverConfig(action: Action<in ServerConfigBuilder>) dsl test
    given("a ratpack server") {
      val app = ratpack {
        serverConfig(Action {
          it.port(1234)
        })
        bindings {}
        handlers {}
      }
      `when`("the application runs") {
        then("serverConfig(action: Action<in ServerConfigBuilder>) works") {
          app.address.toString() shouldBe "http://localhost:1234/"
          app.close()
        }
      }
    }
  }
}
