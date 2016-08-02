package ratpack.kotlin

import com.google.inject.Module
import kotlin.Unit
import kotlin.jvm.functions.Function1
import ratpack.groovy.internal.ClosureUtil
import ratpack.guice.BindingsSpec
import ratpack.kotlin.handling.KChain
import ratpack.server.ServerConfigBuilder
import ratpack.test.embed.EmbeddedApp

import static ratpack.kotlin.handling.RatpackKt.ratpack

//TODO perhaps move to Spek - https://blog.jetbrains.com/kotlin/2014/02/speka-specification-framework/
abstract class RatpackKotlinDslSpec extends EmbeddedBaseDirRatpackSpec {

    protected final List<Module> modules = []
    protected Closure<?> _handlers = ClosureUtil.noop()
    protected Closure<?> _bindings = ClosureUtil.noop()
    protected Closure<?> _serverConfig = ClosureUtil.noop()

    @Delegate
    final EmbeddedApp application = createApplication()

    protected EmbeddedApp createApplication() {
        fromServer {
            ratpack {
                it.serverConfig(createServerConfig())
                it.bindings(createBindings())
                it.handlers(createHandlers())
            }
        }
    }

    Function1<? super ServerConfigBuilder, Unit> createServerConfig() {
        { sb ->
            if (this.baseDir) {
                sb.baseDir(this.baseDir.root)
            }
            sb.port(0)
            sb.with(_serverConfig)
        }
    }

    Function1<? super BindingsSpec, Unit> createBindings() {
        { b ->
            modules.each {
                b.module(it)
            }
            b.with(_bindings)
        }
    }

    Function1<? super KChain, Unit> createHandlers() {
        { c ->
            c.with(_handlers)
        }
    }

    void serverConfig(@DelegatesTo(value = ServerConfigBuilder, strategy = Closure.DELEGATE_FIRST) Closure<?> configurer) {
        _serverConfig = configurer
    }

    void bindings(@DelegatesTo(value = BindingsSpec, strategy = Closure.DELEGATE_FIRST) Closure<?> configurer) {
        _bindings = configurer
    }

    void handlers(@DelegatesTo(value = KChain, strategy = Closure.DELEGATE_FIRST) Closure<?> configurer) {
        _handlers = configurer
    }
}
