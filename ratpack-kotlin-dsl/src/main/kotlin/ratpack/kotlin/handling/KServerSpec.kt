package ratpack.kotlin.handling

import ratpack.func.Action
import ratpack.guice.BindingsSpec
import ratpack.guice.Guice
import ratpack.server.RatpackServerSpec
import ratpack.server.ServerConfigBuilder

class KServerSpec(val delegate: RatpackServerSpec) {
    fun serverConfig(action: Action<in ServerConfigBuilder>) : KServerSpec = KServerSpec(delegate.serverConfig(action))
    inline fun serverConfig(crossinline cb: ServerConfigBuilder.(ServerConfigBuilder) -> Unit) : KServerSpec = KServerSpec(delegate.serverConfig { it.cb(it) })
    inline fun bindings(crossinline cb: BindingsSpec.(BindingsSpec) -> Unit) : KServerSpec = KServerSpec(delegate.registry(Guice.registry { it.cb(it) }))
    inline fun handlers(crossinline cb: KChain.(KChain) -> Unit) : KServerSpec = KServerSpec(delegate.handlers { val c = KChain(it); c.cb(c) })
}
