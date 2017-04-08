package ratpack.kotlin.test.embed

import io.kotlintest.matchers.shouldBe
import io.kotlintest.specs.StringSpec
import ratpack.server.ServerConfig
import ratpack.test.embed.EmbeddedApp

class KotlinEmbeddedAppTest : StringSpec() {
  init {
    "test from" {
      val embeddedApp = EmbeddedApp.of {
        it.registryOf {
          it.add(String::class.java, "root")
        }
        it.handlers {
          it.get {
            it.render(it.get(String::class.java))
          }
        }
      }
      val kotlinApp = KotlinEmbeddedApp.from(embeddedApp)
      kotlinApp.test {
        text shouldBe "root"
      }
    }
    "test from ratpack" {
      ratpack {
        bindings {
          bindInstance(String::class.java, "root")
        }
        handlers {
          get {
            render(get(String::class.java))
          }
        }
      }.test {
        text shouldBe "root"
      }
    }
    "test from server config" {
      fromServer(ServerConfig.of { it.port(9999) }) {
        bindings {
          bindInstance(String::class.java, "root")
        }
        handlers {
          get {
            render(get(String::class.java))
          }
        }
      }.test {
        text shouldBe "root"
      }
    }
    "test from handler" {
      fromHandler {
        render("root")
      }.test {
        text shouldBe "root"
      }
    }
    "test from handlers" {
      fromHandlers {
        get {
          render("root")
        }
      }.test {
        text shouldBe "root"
      }
    }
  }
}

