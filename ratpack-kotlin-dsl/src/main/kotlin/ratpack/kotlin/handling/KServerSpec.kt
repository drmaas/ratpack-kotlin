package ratpack.kotlin.handling

import ratpack.func.Action
import ratpack.guice.BindingsSpec
import ratpack.guice.Guice
import ratpack.handling.Handler
import ratpack.registry.Registry
import ratpack.server.RatpackServerSpec
import ratpack.server.ServerConfig
import ratpack.server.ServerConfigBuilder

class KServerSpec(val delegate: RatpackServerSpec) {

  fun serverConfig(action: Action<in ServerConfigBuilder>): KServerSpec {
    delegate.serverConfig(action)
    return this
  }

  inline fun serverConfig(crossinline cb: ServerConfigBuilder.(ServerConfigBuilder) -> Unit): KServerSpec {
    delegate.serverConfig { it.cb(it) }
    return this
  }

  fun serverConfig(serverConfig: ServerConfig): KServerSpec {
    delegate.serverConfig(serverConfig)
    return this
  }

  inline fun bindings(crossinline cb: BindingsSpec.(BindingsSpec) -> Unit): KServerSpec {
    delegate.registry(Guice.registry { it.cb(it) })
    return this
  }

  inline fun handlers(crossinline cb: KChain.(KChain) -> Unit): KServerSpec {
    delegate.handlers { val c = KChain(it); c.cb(c) }
    return this
  }

  fun handler(handlerFactory: (Registry) -> Handler): KServerSpec {
    delegate.handler { handlerFactory(it) }
    return this
  }

}
