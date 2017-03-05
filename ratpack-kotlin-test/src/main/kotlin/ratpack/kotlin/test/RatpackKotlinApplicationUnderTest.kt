package ratpack.kotlin.test

import ratpack.server.RatpackServer
import ratpack.test.ApplicationUnderTest
import java.net.URI

// TODO extend RatpackServer to get at registry
class RatpackKotlinApplicationUnderTest(val delegate: RatpackServer) : RatpackServer by delegate, ApplicationUnderTest {

  override fun getAddress(): URI {
    if (!delegate.isRunning) {
      delegate.start()
    }
    return URI(delegate.scheme, null, delegate.bindHost, delegate.bindPort, "/", null, null)
  }
}
