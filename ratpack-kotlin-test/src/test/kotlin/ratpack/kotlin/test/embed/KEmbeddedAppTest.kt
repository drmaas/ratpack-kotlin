package ratpack.kotlin.test.embed

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import ratpack.server.ServerConfig

class KEmbeddedAppTest : StringSpec() {
  init {
    "test from" {
      val kotlinApp = KEmbeddedApp.of {
        bindings {
          add(String::class.java, "root")
        }
        handlers {
          get {
            render(get(String::class.java))
          }
        }
      }
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
      fromServer(ServerConfig.of { it.maxHeaderSize(1024) }) {
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

