package ratpack.kotlin.test.embed

import ratpack.func.Action
import ratpack.func.Factory
import ratpack.func.Function
import ratpack.handling.Chain
import ratpack.handling.Context
import ratpack.handling.Handler
import ratpack.handling.Handlers
import ratpack.kotlin.handling.KChain
import ratpack.kotlin.handling.KContext
import ratpack.kotlin.handling.KHandler
import ratpack.kotlin.handling.KHandlers
import ratpack.kotlin.handling.KServerSpec
import ratpack.kotlin.test.embed.internal.KEmbeddedAppSupport
import ratpack.registry.Registry
import ratpack.server.RatpackServer
import ratpack.server.ServerConfig
import ratpack.server.ServerConfigBuilder
import ratpack.server.internal.EmbeddedRatpackServerSpec
import ratpack.test.embed.EmbeddedApp
import ratpack.test.http.TestHttpClient
import ratpack.util.Exceptions.uncheck

/**
 * Creates an {@link EmbeddedApp} from the provided EmbeddedApp.
 */
interface KEmbeddedApp: EmbeddedApp {

  companion object {

    /**
     * Creates an embedded application for the given server.
     *
     * @param server the server to embed
     * @return a newly created embedded application
     */
    fun fromServer(server: RatpackServer): KEmbeddedApp {
      return fromServer(Factory { server })
    }

    /**
     * Creates an embedded application from the given function.
     *
     * @param definition a function that defines the server
     * @return a newly created embedded application
     * @throws java.lang.Exception if an error is encountered creating the application
     * @see ratpack.server.RatpackServer.of
     */
    fun of(definition: Action<in KServerSpec>): KEmbeddedApp {
      return fromServer(RatpackServer.of { d -> definition.execute(KServerSpec(EmbeddedRatpackServerSpec(d))) })
    }

    fun of(definition: KServerSpec.(KServerSpec) -> Unit): KEmbeddedApp {
      return fromServer(RatpackServer.of { d ->
        val spec = KServerSpec(EmbeddedRatpackServerSpec(d))
        definition(spec, spec)
      })
    }

    /**
     * Creates an embedded application for the given server.
     *
     * @param server a factory that creates the server to embed
     * @return a newly created embedded application
     */
    fun fromServer(server: Factory<out RatpackServer>): KEmbeddedApp {
      return object : KEmbeddedAppSupport() {
        override fun createServer(): RatpackServer {
          return server.create()
        }
      }
    }

    /**
     * Creates an embedded application using the given server config, and server creating function.
     *
     * @param serverConfig the server configuration
     * @param definition a function to create the server to embed
     * @return a newly created embedded application
     */
    fun fromServer(serverConfig: ServerConfigBuilder, definition: Action<in KServerSpec>): KEmbeddedApp {
      return fromServer(serverConfig.build(), Action { definition.execute(it) })
    }

    /**
     * Creates an embedded application using the given server config, and server creating function.
     *
     * @param serverConfig the server configuration
     * @param definition a function to create the server to embed
     * @return a newly created embedded application
     */
    fun fromServer(serverConfig: ServerConfig, definition: Action<in KServerSpec>): KEmbeddedApp {
      return fromServer(uncheck<RatpackServer> { RatpackServer.of { b -> definition.execute(KServerSpec(b.serverConfig(serverConfig))) } })
    }

    fun fromServer(serverConfig: ServerConfig, definition: (KServerSpec) -> Unit): KEmbeddedApp {
      return fromServer(uncheck<RatpackServer> { RatpackServer.of { b -> definition(KServerSpec(b.serverConfig(serverConfig))) } })
    }

    /**
     * Creates an embedded application with a default launch config (no base dir, ephemeral port) and the given handler.
     *
     *
     * If you need to tweak the server config, use [.fromServer].
     *
     * @param handlerFactory a handler factory
     * @return a newly created embedded application
     */
    fun fromHandlerFactory(handlerFactory: Function<in Registry, out Handler>): KEmbeddedApp {
      return fromServer(ServerConfig.embedded().build()) { b -> b.handler { handlerFactory.apply(it) } }
    }

    /**
     * Creates an embedded application with a default launch config (no base dir, ephemeral port) and the given handler.
     *
     *
     * If you need to tweak the server config, use [.fromServer].
     *
     * @param handler the application handler
     * @return a newly created embedded application
     */
    fun fromHandler(handler: Handler): KEmbeddedApp {
      return fromServer(ServerConfig.embedded().build()) { b -> b.handler { handler } }
    }

    fun fromHandler(handler: (Context) -> Unit): KEmbeddedApp {
      return fromServer(ServerConfig.embedded().build()) { b ->
        b.handler { Handler { ctx -> handler(ctx) } }
      }
    }

    fun fromKHandler(handler: KHandler): KEmbeddedApp {
      return fromServer(ServerConfig.embedded().build()) { b -> b.handler { handler } }
    }

    fun fromKHandler(handler: (KContext) -> Unit): KEmbeddedApp {
      return fromHandler { ctx: KContext ->  handler(ctx) }
    }

    /**
     * Creates an embedded application with a default launch config (no base dir, ephemeral port) and the given handler chain.
     *
     *
     * If you need to tweak the server config, use [.fromServer].
     *
     * @param action the handler chain definition
     * @return a newly created embedded application
     */
    fun fromHandlers(action: Action<in Chain>): KEmbeddedApp {
      return fromServer(ServerConfig.embedded().build()) { b -> b.handler { r -> Handlers.chain(r.get(ServerConfig::class.java), r, action) } }
    }

    fun fromHandlers(action: (Chain) -> Unit): KEmbeddedApp {
      return fromServer(ServerConfig.embedded().build()) { b -> b.handlers {  }.handler { r -> Handlers.chain(r.get(ServerConfig::class.java), r, action) } }
    }

    fun fromKHandlers(action: Action<in KChain>): KEmbeddedApp {
      return fromServer(ServerConfig.embedded().build()) { b -> b.handler { r -> KHandlers.chain(r.get(ServerConfig::class.java), r, action) } }
    }

    fun fromKHandlers(action: (KChain) -> Unit): KEmbeddedApp {
      return fromServer(ServerConfig.embedded().build()) { b -> b.handlers {  }.handler { r -> KHandlers.chain(r.get(ServerConfig::class.java), r, action) } }
    }

  }

  override fun getServer(): RatpackServer

  fun test(cb: TestHttpClient.(TestHttpClient) -> Unit) {
    try {
      cb(httpClient, httpClient)
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
inline fun ratpack(crossinline cb: KServerSpec.(s: KServerSpec) -> Unit): KEmbeddedApp {
  return KEmbeddedApp.of { it.cb(it) }
}

inline fun fromServer(serverConfig: ServerConfigBuilder, crossinline cb: KServerSpec.(s: KServerSpec) -> Unit): KEmbeddedApp {
  return KEmbeddedApp.fromServer(serverConfig.build()) { it.cb(it) }
}

inline fun fromServer(serverConfig: ServerConfig, crossinline cb: KServerSpec.(s: KServerSpec) -> Unit): KEmbeddedApp {
  return KEmbeddedApp.fromServer(serverConfig) { it.cb(it) }
}

inline fun fromHandler(crossinline cb: KContext.(c: KContext) -> Unit): KEmbeddedApp {
  return KEmbeddedApp.fromHandler { val c = KContext(it); c.cb(c) }
}

inline fun fromHandlers(crossinline cb: KChain.(c: KChain) -> Unit): KEmbeddedApp {
  return KEmbeddedApp.fromHandlers { val c = KChain(it); c.cb(c) }
}

