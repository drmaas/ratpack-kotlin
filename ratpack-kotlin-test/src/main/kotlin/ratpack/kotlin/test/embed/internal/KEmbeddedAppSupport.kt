package ratpack.kotlin.test.embed.internal

import ratpack.kotlin.test.embed.KEmbeddedApp
import ratpack.server.RatpackServer
import ratpack.util.Exceptions.uncheck

abstract class KEmbeddedAppSupport : KEmbeddedApp {

  private var ratpackServer: RatpackServer? = null

  override fun getServer(): RatpackServer {
    if (ratpackServer == null) {
      try {
        ratpackServer = createServer()
      } catch (e: Exception) {
        throw uncheck(e)
      }

    }
    return ratpackServer ?: throw Exception("oops")
  }

  protected abstract fun createServer(): RatpackServer

}
