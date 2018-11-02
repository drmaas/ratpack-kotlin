package ratpack.kotlin.coroutines

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import ratpack.error.ServerErrorHandler
import ratpack.kotlin.test.embed.ratpack
import ratpack.test.embed.EmbeddedApp
import java.util.concurrent.Semaphore
import kotlin.concurrent.thread

private val threadName get() = "(thread: ${Thread.currentThread()}"

/**
 * See https://github.com/gregopet/kotlin-ratpack-coroutines/blob/master/src/test/kotlin/co/petrin/kotlin/BlockingBehaviorSpec.kt
 */
@kotlinx.coroutines.ExperimentalCoroutinesApi
class BlockingBehaviorSpec : Spek({
  given("An embedded app with a blocking & non-blocking operation") {
    lateinit var semaphore: Semaphore
    lateinit var embeddedApp: EmbeddedApp

    beforeEachTest {
      semaphore = Semaphore(1)
      embeddedApp = ratpack {
        serverConfig { threads(1) }
        handlers {
          register {
            add(ServerErrorHandler { context, throwable ->
              context.response.status(500).send("CUSTOM ERROR HANDLER $throwable")
            })
          }

          get("sleepblock") {
            println("INSIDE SLEEPBLOCK HANDLER $threadName")
            semaphore.release()
            Thread.sleep(1000 * 5)
            println("EXITING SLEEPBLOCK HANDLER $threadName")
            send("SLEEP")
          }

          get("sleep") {
            println("INSIDE SLEEP HANDLER $threadName")
            semaphore.release()

            async {
              println("SLEEPING ASYNC $threadName")
              var message = await {
                println("SLEEPING BLOCK ASYNC $threadName")
                Thread.sleep(1000 * 5)
                "WOKE UP"
              }
              println("SLEEPING ASYNC OVER $threadName")
              send(message)
            }
            println("EXITING SLEEP HANDLER $threadName")

          }

          get("quick") {
            println("INSIDE QUICK HANDLER $threadName")
            Thread.sleep(100)
            send("ECHO $threadName")
          }

          get("throw") {
            async {
              println("INSIDE ASYNC EXCEPTION THROWING HANDLER $threadName")
              Thread.sleep(100)
              println("INSIDE ASYNC EXCEPTION THROWING HANDLER, ABOUT TO THROW $threadName")
              throw IllegalStateException("Except me!")
            }
          }

          get("throwcatch") {
            async {
              println("INSIDE ASYNC EXCEPTION THROWING  AND CATCHING HANDLER $threadName")
              Thread.sleep(100)
              try {
                println("INSIDE ASYNC EXCEPTION THROWING HANDLER, ABOUT TO THROW $threadName")
                throw IllegalStateException("Except me!")
              } catch(t: Throwable) {
                render("Caught it")
              }
            }
          }

          post("echobody") {
            println("INSIDE ECHOBODY HANDLER $threadName")
            async {
              println("INSIDE ECHOBODY ASYNC $threadName")
              val txt = request.body.await().text
              println("INSIDE ECHOBODY ASYNC AFTER COROUTINE $threadName")
              send(txt)
            }
          }
        }
      }
    }

    afterEachTest {
      embeddedApp.close()
    }

    test("A blocking function will clog our test app") {
      var blockingSleepFinished = false
      var simpleRequestFinished = false
      semaphore.acquire()
      thread(start = true, isDaemon = true) {
        try {
          embeddedApp.httpClient.getText("sleepblock").let(::println)
          blockingSleepFinished = true
        } catch (x: Exception) {}
      }

      semaphore.acquire()

      thread(start = true, isDaemon = true) {
        semaphore.release()
        embeddedApp.httpClient.getText("quick").let(::println)
        simpleRequestFinished = true
      }
      semaphore.acquire()
      Thread.sleep(1000)

      check(!blockingSleepFinished) { "Blocking should still be computing" }
      check(!simpleRequestFinished) { "Quick thread should be waiting on the blocking head"}
    }


    test("A non-blocking function will not clog our test app") {

      var blockingSleepFinished = false
      var simpleRequestFinished = false

      semaphore.acquire()
      thread(start = true, isDaemon = true) {
        try {
          val response = embeddedApp.httpClient.getText("sleep")
          check(response == "WOKE UP")
          blockingSleepFinished = true
        } catch (x: Exception) {}
      }
      semaphore.acquire()

      thread(start = true, isDaemon = true) {
        try {
          semaphore.release()
          embeddedApp.httpClient.getText("quick").let(::println)
          simpleRequestFinished = true
        } catch (x: Exception) {}
      }
      semaphore.acquire()
      Thread.sleep(1000)

      check(!blockingSleepFinished)  { "Blocking should still be computing" }
      check(simpleRequestFinished)  { "Quick thread should NOT be waiting on the blocking head"}
    }

    test("A non-blocking function will return the correct result") {
      check(embeddedApp.httpClient.getText("sleep") == "WOKE UP")
    }

    test("await() can be called on Ratpack promises") {
      val requestWithBody = embeddedApp.httpClient.requestSpec { it.body.text("foobar") }
      val response = requestWithBody.postText("echobody")
      check(response == "foobar") { "Response $response should be 'foobar'" }
    }

    test("exceptions are handled by Ratpack") {
      val response = embeddedApp.httpClient.get("throw")
      val responseText = response.body.text
      check(response.status.code == 500) { "Response code should be '500' after exception, was ${response.status.code}" }
      check(responseText.startsWith("CUSTOM ERROR HANDLER")) { "Server should render the error page after an exception, rendered $responseText instead" }
    }

    test("exceptions can be caught and thus not handled by Ratpack") {
      val response = embeddedApp.httpClient.get("throwcatch")
      val responseText = response.body.text
      check(response.status.code == 200) { "Response code should be '200' after caught exception, was ${response.status.code}" }
      check(responseText == "Caught it"){ "Server should have caught the exception but it rendered $responseText instead" }
    }
  }
})
