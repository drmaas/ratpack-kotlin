package ratpack.kotlin.handling

import kotlinx.coroutines.ExperimentalCoroutinesApi
import ratpack.func.Action
import ratpack.handling.Chain
import ratpack.handling.Handler
import ratpack.handling.Handlers
import ratpack.kotlin.coroutines.async
import ratpack.registry.Registry
import ratpack.server.ServerConfig

object KHandlers {

  @OptIn(ExperimentalCoroutinesApi::class)
  inline fun from(crossinline cb: suspend KContext.(KContext) -> Unit): Handler = { ctx: KContext -> ctx.async { ctx.cb(ctx) } } as Handler

  fun chain(serverConfig: ServerConfig, action: Action<in KChain>): Handler {
    return chain(serverConfig, Registry.empty(), action)
  }

  fun chain(serverConfig: ServerConfig, registry: Registry, action: Action<in KChain>): Handler {
    val chainAction: Action<in Chain> = Action { chain -> action.execute(KChain(chain)) }
    return Handlers.chain(serverConfig, registry, chainAction)
  }

  fun chain(serverConfig: ServerConfig, registry: Registry, action: (KChain) -> Unit): Handler {
    val chainAction: Action<in Chain> = Action { chain -> action(KChain(chain)) }
    return Handlers.chain(serverConfig, registry, chainAction)
  }

  fun chain(registry: Registry, action: Action<in KChain>): Handler {
    return chain(registry.get(), registry, action)
  }

  fun chain(handlers: List<KHandler>): Handler {
    return Handlers.chain(handlers)
  }

  fun chain(vararg handlers: KHandler): Handler {
    return Handlers.chain(*handlers)
  }

}
