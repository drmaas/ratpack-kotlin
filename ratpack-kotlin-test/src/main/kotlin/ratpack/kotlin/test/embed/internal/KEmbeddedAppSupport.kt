package ratpack.kotlin.test.embed.internal

import ratpack.kotlin.handling.KRatpackServer
import ratpack.kotlin.test.embed.KEmbeddedApp
import ratpack.util.Exceptions.uncheck

abstract class KEmbeddedAppSupport : KEmbeddedApp {

  private var ratpackServer: KRatpackServer? = null

  override fun getServer(): KRatpackServer {
    if (ratpackServer == null) {
      try {
        ratpackServer = createServer()
      } catch (e: Exception) {
        throw uncheck(e)
      }

    }
    return ratpackServer ?: throw Exception("oops")
  }

  protected abstract fun createServer(): KRatpackServer

}
