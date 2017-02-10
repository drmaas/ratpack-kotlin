package ratpack.kotlin.test

import ratpack.server.RatpackServer
import ratpack.test.ApplicationUnderTest
import java.net.URI

class RatpackKotlinApplicationUnderTest(val server: RatpackServer) : RatpackServer by server, ApplicationUnderTest {

  override fun getAddress(): URI {
    if (!server.isRunning) {
      server.start()
    }
    return URI(server.scheme, null, server.bindHost, server.bindPort, "/", null, null)
  }
}
