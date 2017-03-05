package ratpack.kotlin.handling

import ratpack.guice.BindingsSpec
import ratpack.guice.Guice
import ratpack.server.RatpackServerSpec
import ratpack.server.ServerConfigBuilder

class KServerSpec(val delegate: RatpackServerSpec) {
    fun serverConfig(cb: ServerConfigBuilder.() -> Unit) : KServerSpec = KServerSpec(delegate.serverConfig { it.cb() })
    fun bindings(cb: BindingsSpec.() -> Unit) : KServerSpec = KServerSpec(delegate.registry(Guice.registry { it.cb() }))
    fun handlers(cb: KChain.() -> Unit) : KServerSpec = KServerSpec(delegate.handlers { KChain(it).cb() })
}
