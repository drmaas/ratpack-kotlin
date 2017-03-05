package ratpack.kotlin.handling

import ratpack.file.FileHandlerSpec
import ratpack.func.Action
import ratpack.func.Predicate
import ratpack.handling.Chain
import ratpack.handling.Context
import ratpack.handling.Handler
import ratpack.registry.Registry
import ratpack.registry.RegistrySpec
import ratpack.server.ServerConfig

/**
 * Allow for the kotlin DSL to be used from classes extending KChainAction.
 */
abstract class KChainAction : Action<Chain> {
  var delegate: ThreadLocal<KChain> = ThreadLocal()

  abstract fun execute()

  override fun execute(chain: Chain) {
    delegate.set(KChain(chain))
    execute()
    delegate.remove()
  }

  //convenience to get delegate value
  fun dg(): KChain = delegate.get()

  //mirror of available functions in GroovyChain

  //misc
  fun getRegistry(): Registry = dg().registry

  fun getServerConfig(): ServerConfig = dg().serverConfig

  //prefix
  fun prefix(path: String = "", cb: KChain.() -> Unit): KChain = dg().prefix(path, cb)
  fun prefix(path: String = "", action: Action<in Chain>): KChain = dg().prefix(path, action)
  fun prefix(path: String = "", action: Class<out Action<in Chain>>): KChain = dg().prefix(path, action)

  //all
  fun all(cb: KContext.() -> Unit): KChain = dg().all(cb)
  fun all(handler: Handler): KChain = dg().all(handler)
  fun all(handler: Class<Handler>): KChain = dg().all(handler)

  //path
  fun path(path: String = "", cb: KContext.() -> Unit): KChain = dg().path(path, cb)
  fun path(path: String = "", handler: Handler): KChain = dg().path(path, handler)
  fun path(path: String = "", handler: Class<Handler>): KChain = dg().path(path, handler)

  //chain
  fun chain(cb: KChain.() -> Unit): Handler = dg().chain(cb)
  fun chain(action: Class<out Action<in Chain>>): Handler = dg().chain(action)

  //get
  fun get(path: String = "", cb: KContext.() -> Unit): KChain = dg().get(path, cb)
  fun get(path: String = "", handler: Handler): KChain = dg().get(path, handler)
  fun get(path: String = "", handler: Class<Handler>): KChain = dg().get(path, handler)

  //put
  fun put(path: String = "", cb: KContext.() -> Unit): KChain = dg().put(path, cb)
  fun put(path: String = "", handler: Handler): KChain = dg().put(path, handler)
  fun put(path: String = "", handler: Class<Handler>): KChain = dg().put(path, handler)

  //post
  fun post(path: String = "", cb: KContext.() -> Unit): KChain = dg().post(path, cb)
  fun post(path: String = "", handler: Handler): KChain = dg().post(path, handler)
  fun post(path: String = "", handler: Class<Handler>): KChain = dg().post(path, handler)

  //delete
  fun delete(path: String = "", cb: KContext.() -> Unit): KChain = dg().delete(path, cb)
  fun delete(path: String = "", handler: Handler): KChain = dg().delete(path, handler)
  fun delete(path: String = "", handler: Class<Handler>): KChain = dg().delete(path, handler)

  //patch
  fun patch(path: String = "", cb: KContext.() -> Unit): KChain = dg().patch(path, cb)
  fun patch(path: String = "", handler: Handler): KChain = dg().patch(path, handler)
  fun patch(path: String = "", handler: Class<Handler>): KChain = dg().patch(path, handler)

  //options
  fun options(path: String = "", cb: KContext.() -> Unit): KChain = dg().options(path, cb)
  fun options(path: String = "", handler: Handler): KChain = dg().options(path, handler)
  fun options(path: String = "", handler: Class<Handler>): KChain = dg().options(path, handler)

  //fileSystem
  fun fileSystem(path: String = "", cb: KChain.() -> Unit): KChain = dg().fileSystem(path, cb)
  fun fileSystem(path: String = "", action: Action<in Chain>): KChain = dg().fileSystem(path, action)
  fun fileSystem(path: String = "", action: Class<out Action<in Chain>>): KChain = dg().fileSystem(path, action)

  //files
  fun files(cb: FileHandlerSpec.() -> Unit): KChain = dg().files(cb)
  fun files(): KChain = dg().files()
  fun files(action: Action<in FileHandlerSpec>): KChain = dg().files(action)

  //host
  fun host(hostName: String, cb: KChain.() -> Unit): KChain = dg().host(hostName, cb)
  fun host(hostName: String, action: Action<in Chain>): KChain = dg().host(hostName, action)
  fun host(hostName: String, action: Class<out Action<in Chain>>): KChain = dg().host(hostName, action)

  //insert
  fun insert(cb: KChain.() -> Unit): KChain = dg().insert(cb)
  fun insert(action: Action<in Chain>): KChain = dg().insert(action)
  fun insert(action: Class<out Action<in Chain>>): KChain = dg().insert(getRegistry().get(action))

  //redirect
  fun redirect(code: Int, location: String): KChain = dg().redirect(code, location)

  //register
  fun register(registry: Registry): KChain = dg().register(registry)

  fun register(action: Action<in RegistrySpec>): KChain = dg().register(action)
  fun register(registry: Registry, action: Action<in Chain>): KChain = dg().register(registry, action)
  fun register(registry: Registry, action: Class<out Action<in Chain>>): KChain = dg().register(registry, action)
  fun register(registryAction: Action<in RegistrySpec>, chainAction: Action<in Chain>): KChain = dg().register(registryAction, chainAction)
  fun register(registryAction: Action<in RegistrySpec>, chainAction: Class<out Action<in Chain>>): KChain = dg().register(registryAction, chainAction)
  fun register(registryAction: RegistrySpec.() -> Unit, cb: KChain.() -> Unit): KChain = dg().register(registryAction, cb)
  fun register(registry: Registry, cb: KChain.() -> Unit): KChain = dg().register(registry, cb)
  fun register(registryAction: RegistrySpec.() -> Unit): KChain = dg().register(registryAction)

  //when
  fun `when`(test: Predicate<in Context>, cb: KChain.() -> Unit): KChain = dg().`when`(test, cb)

  fun `when`(test: Predicate<in Context>, action: Action<in Chain>): KChain = dg().`when`(test, action)
  fun `when`(test: Predicate<in Context>, action: Class<out Action<in Chain>>): KChain = dg().`when`(test, action)
  fun `when`(test: Boolean, cb: KChain.() -> Unit): KChain = dg().`when`(test, cb)
  fun `when`(test: Boolean, action: Action<in Chain>): KChain = dg().`when`(test, action)
  fun `when`(test: Boolean, action: Class<out Action<in Chain>>): KChain = dg().`when`(test, action)

  //onlyIf
  fun onlyIf(test: Predicate<in Context>, cb: KContext.() -> Unit): KChain = dg().onlyIf(test, cb)
  fun onlyIf(test: Predicate<in Context>, handler: Handler): KChain = dg().onlyIf(test, handler)
  fun onlyIf(test: Predicate<in Context>, handler: Class<out Handler>): KChain = dg().onlyIf(test, handler)

  //notFound
  fun notFound(): KChain = dg().notFound()
}
