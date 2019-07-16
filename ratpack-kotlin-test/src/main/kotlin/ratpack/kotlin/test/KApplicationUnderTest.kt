package ratpack.kotlin.test

import ratpack.kotlin.test.embed.KEmbeddedApp
import ratpack.registry.Registry
import ratpack.server.RatpackServer
import ratpack.server.internal.ServerCapturer
import ratpack.test.ApplicationUnderTest
import ratpack.test.MainClassApplicationUnderTest
import ratpack.test.http.TestHttpClient
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method
import java.lang.reflect.Modifier
import java.net.URI
import kotlin.reflect.KClass

/**
 * Create a kotlin application under test from a class with a main method.
 */
fun kotlinApplicationUnderTest(mainClass: KClass<*>) : KMainClassApplicationUnderTest {
  return KMainClassApplicationUnderTest(mainClass)
}

/**
 * Create a kotlin application under test from a fully qualified url.
 */
fun kotlinApplicationUnderTest(url: String): KApplicationUnderTest {
  return object: KApplicationUnderTest {
    override fun getAddress(): URI {
      return URI(if (url.endsWith("/")) { url } else { "$url/" })
    }
  }
}

/**
 * Create a kotlin application under test from a RatpackServer object.
 */
fun kotlinApplicationUnderTest(server: RatpackServer): KCloseableApplicationUnderTest {
  return object: KCloseableApplicationUnderTest {
    override fun close() {
      server.stop()
    }
    override fun getAddress(): URI {
      if (!server.isRunning) {
        server.start()
      }
      return URI(server.scheme, null, server.bindHost, server.bindPort, "/", null, null)
    }
    override fun getRegistry(): Registry {
      return server.registry.orElse(Registry.empty())
    }
  }
}

/**
 * Create a kotlin application under test from an embedded app.
 */
fun kotlinApplicationUnderTest(app: KEmbeddedApp): KCloseableApplicationUnderTest {
  return kotlinApplicationUnderTest(app.server)
}

/**
 * Create a test http client from and application under test.
 */
fun testHttpClient(aut: ApplicationUnderTest): TestHttpClient {
  return TestHttpClient.testHttpClient(aut)
}

interface KApplicationUnderTest: ApplicationUnderTest {
  fun getRegistry(): Registry {
    return Registry.empty()
  }
}

interface KCloseableApplicationUnderTest: KApplicationUnderTest, AutoCloseable

open class KMainClassApplicationUnderTest(val mainClass: KClass<*>): MainClassApplicationUnderTest(mainClass.java), KApplicationUnderTest {

  private var server: RatpackServer? = null

  override fun createServer(): RatpackServer {
    server = ServerCapturer.capture {
      val method: Method
      try {
        method = mainClass.java.getDeclaredMethod("main", Array<String>::class.java)
      } catch (e: NoSuchMethodException) {
        throw IllegalStateException("Class" + mainClass.java.name + " does not have a main(String...) class")
      }

      if (!Modifier.isStatic(method.modifiers)) {
        throw IllegalStateException(mainClass.java.name + ".main() must be static")
      }

      try {
        method.invoke(null, arrayOf<String>())
      } catch (e: IllegalAccessException) {
        throw IllegalStateException("Could not invoke " + mainClass.java.name + ".main()", e)
      } catch (e: IllegalArgumentException) {
        throw IllegalStateException("Could not invoke " + mainClass.java.name + ".main()", e)
      } catch (e: InvocationTargetException) {
        throw IllegalStateException("Could not invoke " + mainClass.java.name + ".main()", e)
      }

    }
    return server ?: throw IllegalStateException(mainClass.java.name + ".main() did not start a Ratpack server")
  }

  override fun getRegistry(): Registry {
    return server?.registry?.orElse(Registry.empty()) ?: Registry.empty()
  }
}
