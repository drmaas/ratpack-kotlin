package ratpack.kotlin.test.embed

import ratpack.kotlin.handling.KChain
import ratpack.kotlin.handling.KContext
import ratpack.kotlin.handling.KServerSpec
import ratpack.kotlin.test.embed.KotlinEmbeddedApp.Companion.from
import ratpack.kotlin.test.embed.internal.DefaultKotlinEmbeddedApp
import ratpack.server.ServerConfig
import ratpack.server.ServerConfigBuilder
import ratpack.test.embed.EmbeddedApp
import ratpack.test.http.TestHttpClient

/**
 * Creates an {@link EmbeddedApp} from the provided EmbeddedApp.
 */
interface KotlinEmbeddedApp: EmbeddedApp {

  companion object {
    fun from(embeddedApp: EmbeddedApp): KotlinEmbeddedApp {
      return DefaultKotlinEmbeddedApp(embeddedApp)
    }
  }

  fun test(cb: TestHttpClient.() -> Unit) {
    try {
      cb.invoke(httpClient)
    } finally {
      close()
    }
  }

}

/**
 * Creates an {@link EmbeddedApp} from the provided closure.
 * <p>
 * <pre class="tested">
 * import static ratpack.kotlin.test.embed.ratpack
 *
 * ratpack {
 *   bindings {
 *     bindInstance String, "root"
 *   }
 *   handlers {
 *     get {
 *       render get(String)
 *     }
 *   }
 * } test {
 *   assert getText() == "root"
 * }
 * </pre>
 *
 * @param script the application definition
 * @return a  Ratpack application.
 */
inline fun ratpack(crossinline cb: KServerSpec.() -> Unit): KotlinEmbeddedApp {
  return from(EmbeddedApp.of { s -> KServerSpec(s).cb() })
}

inline fun fromServer(serverConfig: ServerConfigBuilder, crossinline cb: KServerSpec.() -> Unit): KotlinEmbeddedApp {
  return from(EmbeddedApp.fromServer(serverConfig.build(), { s -> KServerSpec(s).cb() }))
}

inline fun fromServer(serverConfig: ServerConfig, crossinline cb: KServerSpec.() -> Unit): KotlinEmbeddedApp {
  return from(EmbeddedApp.fromServer(serverConfig, { s -> KServerSpec(s).cb() }))
}

inline fun fromHandler(crossinline cb: KContext.() -> Unit): KotlinEmbeddedApp {
  return from(EmbeddedApp.fromHandler { ctx -> KContext(ctx).cb() })
}

inline fun fromHandlers(crossinline cb: KChain.() -> Unit): KotlinEmbeddedApp {
  return from(EmbeddedApp.fromHandlers { chain -> KChain(chain).cb() })
}

