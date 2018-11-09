package ratpack.kotlin.handling

import ratpack.func.Action
import ratpack.impose.Impositions
import ratpack.registry.Registry
import ratpack.server.RatpackServer
import ratpack.server.RatpackServerSpec
import ratpack.server.internal.DefaultRatpackServer

/*
 * Kotlin DSL top-level function.
 */
fun ratpack(cb: KServerSpec.(KServerSpec) -> Unit) = KRatpackServer.start(cb)

interface KRatpackServer : RatpackServer {

  fun getRegistry(): Registry

  companion object {

    fun of(definition: Action<in RatpackServerSpec>): KRatpackServer {
      return DefaultKRatpackServer(definition, Impositions.current())
    }

    fun of(definition: KServerSpec.(KServerSpec) -> Unit): KRatpackServer {
      return of(Action { val s = KServerSpec(it); s.definition(s) })
    }

    fun start(definition: Action<in RatpackServerSpec>): KRatpackServer {
      val server = of(definition)
      server.start()
      return server
    }

    fun start(definition: KServerSpec.(KServerSpec) -> Unit): KRatpackServer {
      return start(Action { val s = KServerSpec(it); s.definition(s) })
    }

  }

}

class DefaultKRatpackServer(definitionFactory: Action<in RatpackServerSpec>, impositions: Impositions) : KRatpackServer, DefaultRatpackServer(definitionFactory, impositions) {

   override fun getRegistry(): Registry {
    return serverRegistry
  }

}
