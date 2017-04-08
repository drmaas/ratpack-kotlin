package ratpack.kotlin.test

import ratpack.server.RatpackServer
import ratpack.test.ApplicationUnderTest
import ratpack.test.CloseableApplicationUnderTest
import ratpack.test.MainClassApplicationUnderTest
import ratpack.test.http.TestHttpClient
import java.net.URI
import kotlin.reflect.KClass

/**
 * Create a kotlin application under test from a class with a main method.
 */
fun kotlinApplicationUnderTest(mainClass: KClass<*>) : CloseableApplicationUnderTest {
  return MainClassApplicationUnderTest(mainClass.java)
}

/**
 * Create a kotlin application under test from a fully qualified url.
 */
fun kotlinApplicationUnderTest(url: String): ApplicationUnderTest {
  return ApplicationUnderTest { URI(if (url.endsWith("/")) { url } else { url + "/" }) }
}

/**
 * Create a kotlin application under test from a RatpackServer object.
 */
fun kotlinApplicationUnderTest(server: RatpackServer): CloseableApplicationUnderTest {
  return object: CloseableApplicationUnderTest {
    override fun close() {
      server.stop()
    }
    override fun getAddress(): URI {
      if (!server.isRunning) {
        server.start()
      }
      return URI(server.scheme, null, server.bindHost, server.bindPort, "/", null, null)
    }
  }
}

/**
 * Create a test http client from and application under test.
 */
fun testHttpClient(aut: ApplicationUnderTest): TestHttpClient {
  return TestHttpClient.testHttpClient(aut)
}
