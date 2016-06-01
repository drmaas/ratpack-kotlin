package ratpack.kotlin.handling

import ratpack.guice.BindingsSpec
import ratpack.guice.Guice
import ratpack.server.RatpackServerSpec
import ratpack.server.ServerConfigBuilder

class KServerSpec(val delegate: RatpackServerSpec) : RatpackServerSpec by delegate {
    fun serverConfig(cb: ServerConfigBuilder.() -> Unit) = delegate.serverConfig { it.(cb)() }
    fun bindings(cb: BindingsSpec.() -> Unit) = delegate.registry(Guice.registry { bindings: BindingsSpec -> bindings.(cb)() })
    fun handlers(cb: KChain.() -> Unit) = delegate.handlers { KChain(it).(cb)() }
}
