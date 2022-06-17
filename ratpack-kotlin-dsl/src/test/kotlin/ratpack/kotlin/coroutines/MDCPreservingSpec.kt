package ratpack.kotlin.coroutines

import io.kotest.core.spec.Spec
import io.kotest.core.spec.style.StringSpec
import org.slf4j.MDC
import ratpack.error.ServerErrorHandler
import ratpack.kotlin.test.embed.ratpack
import ratpack.logging.MDCInterceptor
import ratpack.test.embed.EmbeddedApp

/**
 * See https://github.com/gregopet/kotlin-ratpack-coroutines/blob/master/src/test/kotlin/co/petrin/kotlin/BlockingBehaviorSpec.kt
 */
class MDCPreservingSpec : StringSpec() {

  lateinit var embeddedApp: EmbeddedApp

  override suspend fun beforeSpec(spec: Spec) {
    embeddedApp = ratpack {
      bindings {
        bindInstance(MDCInterceptor.instance())
        bindInstance(ServerErrorHandler::class.java, ServerErrorHandler { context, _ ->
          context.response.send(MDC.get("key"))
        })
      }
      handlers {
        get("simplecall") {
          async {
            MDC.put("key", "value")
            check(MDC.get("key") == "value") { "Values can be obtained from the MDC" }
            val reply = await { MDC.get("key") } ?: "Key was not present in MDC"
            send(reply)
          }
        }

        get("mixedexecution") {
          MDC.put("key", "value")
          check(MDC.get("key") == "value") { "Values can be obtained from the MDC" }
          async {
            await { Thread.sleep(100) }
          }
          send(MDC.get("key"))
        }

        get("caughtexception") {
          async {
            MDC.put("key", "value")
            check(MDC.get("key") == "value") { "Values can be obtained from the MDC" }
            try {
              await { throw IllegalStateException() }
            } catch (t: Throwable) {
              val reply = await { MDC.get("key") } ?: "Key was not present in MDC"
              send(reply)
            }
          }
        }

        get("uncaughtexception") {
          async {
            MDC.put("key", "value")
            check(MDC.get("key") == "value") { "Values can be obtained from the MDC" }
            await { throw IllegalStateException() }
          }
        }
      }
    }
  }

  override suspend fun afterSpec(spec: Spec) {
    embeddedApp.close()
  }

  init {
    "The MDC is preserved inside await blocks" {
      val response = embeddedApp.httpClient.getText("simplecall")
      check(response == "value") { "Response should have been 'value' but was '$response'" }
    }

    "The MDC is preserved inside mixed sync/async handling" {
      val response = embeddedApp.httpClient.getText("mixedexecution")
      check(response == "value") { "Response should have been 'value' but was '$response'" }
    }

    "The MDC is preserved during exception handling" {
      val response = embeddedApp.httpClient.getText("caughtexception")
      check(response == "value") { "Response should have been 'value' but was '$response'" }
    }

    "The MDC is preserved for the default exception handler" {
      val response = embeddedApp.httpClient.getText("uncaughtexception")
      check(response == "value") { "Response should have been 'value' but was '$response'" }
    }
  }
}
