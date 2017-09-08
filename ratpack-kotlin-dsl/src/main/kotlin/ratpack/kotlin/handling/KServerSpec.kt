package ratpack.kotlin.handling

import ratpack.guice.BindingsSpec
import ratpack.guice.Guice
import ratpack.server.RatpackServerSpec
import ratpack.server.ServerConfigBuilder

class KServerSpec(val delegate: RatpackServerSpec) {
    inline fun serverConfig(crossinline cb: ServerConfigBuilder.(s: ServerConfigBuilder) -> Unit) : KServerSpec = KServerSpec(delegate.serverConfig { it.cb(it) })
    inline fun bindings(crossinline cb: BindingsSpec.(b: BindingsSpec) -> Unit) : KServerSpec = KServerSpec(delegate.registry(Guice.registry { it.cb(it) }))
    inline fun handlers(crossinline cb: KChain.(c: KChain) -> Unit) : KServerSpec = KServerSpec(delegate.handlers { val c = KChain(it); c.cb(c) })
}
