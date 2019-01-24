package ratpack.kotlin.test

import io.kotlintest.matchers.shouldEqual
import io.kotlintest.matchers.shouldNotBe
import io.kotlintest.specs.StringSpec
import ratpack.kotlin.test.embed.ratpack
import ratpack.server.RatpackServer

class RatpackKotlinApplicationUnderTestTest: StringSpec() {
  init {
    "test an app via main class" {
      val aut = kotlinApplicationUnderTest(SampleApp::class)
      val client = testHttpClient(aut)
      val response = client.get()
      response.statusCode shouldEqual 200
      response.body.text shouldEqual "foo"
      aut.close()
    }

    "test an app via url" {
      val app = sampleApp()
      app.start()
      val aut = kotlinApplicationUnderTest("${app.scheme}://${app.bindHost}:${app.bindPort}")
      val client = testHttpClient(aut)
      val response = client.get()
      response.statusCode shouldEqual 200
      response.body.text shouldEqual "foo"
      app.stop()
    }

    "test an app via RatpackServer" {
      val aut = kotlinApplicationUnderTest(sampleApp())
      val client = testHttpClient(aut)
      val response = client.get()
      response.statusCode shouldEqual 200
      response.body.text shouldEqual "foo"
      aut.getRegistry() shouldNotBe null
      aut.close()
    }

  }
}

class SampleApp {
  companion object {
    @JvmStatic
    fun main(args: Array<String>) {
      ratpack {
        serverConfig {
          port(8089)
        }
        handlers {
          get {
            render("foo")
          }
        }
      }
    }
  }
}

fun sampleApp(): RatpackServer {
  return ratpack {
    serverConfig {
      port(8089)
    }
    handlers {
      get {
        render("foo")
      }
    }
  }.server
}
