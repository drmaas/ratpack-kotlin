package ratpack.kotlin.handling

import ratpack.guice.BindingsSpec
import ratpack.guice.Guice
import ratpack.server.RatpackServerSpec
import ratpack.server.ServerConfigBuilder

class KServerSpec(val delegate: RatpackServerSpec) {
    inline fun serverConfig(crossinline cb: ServerConfigBuilder.() -> Unit) : KServerSpec = KServerSpec(delegate.serverConfig { it.cb() })
    inline fun bindings(crossinline cb: BindingsSpec.() -> Unit) : KServerSpec = KServerSpec(delegate.registry(Guice.registry { it.cb() }))
    inline fun handlers(crossinline cb: KChain.() -> Unit) : KServerSpec = KServerSpec(delegate.handlers { KChain(it).cb() })
}
