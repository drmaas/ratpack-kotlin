package ratpack.kotlin.test.embed.internal

import ratpack.kotlin.test.embed.KotlinEmbeddedApp
import ratpack.server.RatpackServer
import ratpack.test.embed.EmbeddedApp
import ratpack.test.http.TestHttpClient
import java.net.URI

class DefaultKotlinEmbeddedApp(val delegate: EmbeddedApp): KotlinEmbeddedApp {

  override fun getServer(): RatpackServer {
    return delegate.server
  }

  override fun getAddress(): URI {
    return delegate.address
  }

  override fun getHttpClient(): TestHttpClient {
    return delegate.httpClient
  }

  override fun close() {
    delegate.close()
  }
}
