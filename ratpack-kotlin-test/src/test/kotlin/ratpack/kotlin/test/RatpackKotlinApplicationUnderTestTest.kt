package ratpack.kotlin.test

import io.kotlintest.shouldBe
import io.kotlintest.shouldNotBe
import io.kotlintest.specs.StringSpec
import ratpack.guice.BindingsImposition
import ratpack.impose.ImpositionsSpec
import ratpack.kotlin.test.embed.ratpack
import ratpack.server.RatpackServer

class RatpackKotlinApplicationUnderTestTest: StringSpec() {
  init {
    "test an app via main class" {
      val aut = kotlinApplicationUnderTest(SampleApp::class)
      val client = testHttpClient(aut)
      val response = client.get()
      response.statusCode shouldBe 200
      response.body.text shouldBe "foo"
      aut.close()
    }

    "test an app via url" {
      val app = sampleApp()
      app.start()
      val aut = kotlinApplicationUnderTest("${app.scheme}://${app.bindHost}:${app.bindPort}")
      val client = testHttpClient(aut)
      val response = client.get()
      response.statusCode shouldBe 200
      response.body.text shouldBe "foo"
      app.stop()
    }

    "test an app via RatpackServer" {
      val aut = kotlinApplicationUnderTest(sampleApp())
      val client = testHttpClient(aut)
      val response = client.get()
      response.statusCode shouldBe 200
      response.body.text shouldBe "foo"
      aut.getRegistry() shouldNotBe null
      aut.close()
    }

    "test an app via main class with impositions" {
      val aut = object : KMainClassApplicationUnderTest(SampleApp::class) {
        override fun addImpositions(impositions: ImpositionsSpec?) {
          impositions?.add(BindingsImposition.of { it.bindInstance(String::class.java, "bar") })
        }
      }

      aut.test {client ->
        val response = client.get()
        response.statusCode shouldBe 200
        response.body.text shouldBe "bar"
        aut.getRegistry() shouldNotBe null
      }
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
        bindings {
          bindInstance(String::class.java, "foo")
        }
        handlers {
          get {
            render(get(String::class.java))
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
