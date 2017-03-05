package ratpack.kotlin.handling

import ratpack.file.FileHandlerSpec
import ratpack.func.Action
import ratpack.func.Predicate
import ratpack.handling.Chain
import ratpack.handling.Context
import ratpack.handling.Handler
import ratpack.registry.Registry
import ratpack.registry.RegistrySpec

/**
 * Method delegation inside of 'handlers' dsl.
 */
class KChain(val delegate: Chain) {

  val registry = delegate.registry!!
  val serverConfig = delegate.serverConfig!!

  fun chain(cb: KChain.() -> Unit): Handler = delegate.chain { KChain(it).cb() }
  fun chain(action: Class<out Action<in Chain>>): Handler {
    return delegate.chain(action)
  }

  fun files(cb: FileHandlerSpec.() -> Unit): KChain = KChain(delegate.files { it.cb() })
  fun files(): KChain = KChain(delegate.files())
  fun files(action: Action<in FileHandlerSpec>): KChain = KChain(delegate.files(action))

  fun fileSystem(path: String = "", cb: KChain.() -> Unit): KChain = KChain(delegate.fileSystem(path) { KChain(it).cb() })
  fun fileSystem(path: String = "", action: Action<in Chain>): KChain = KChain(delegate.fileSystem(path, action))
  fun fileSystem(path: String = "", action: Class<out Action<in Chain>>): KChain = KChain(delegate.fileSystem(path, action))

  fun prefix(path: String = "", cb: KChain.() -> Unit): KChain = KChain(delegate.prefix(path) { KChain(it).cb() })
  fun prefix(path: String = "", action: Action<in Chain>): KChain = KChain(delegate.prefix(path, action))
  fun prefix(path: String = "", action: Class<out Action<in Chain>>): KChain = KChain(delegate.prefix(path, action))

  fun all(cb: KContext.() -> Unit): KChain = KChain(delegate.all { KContext(it).cb() })
  fun all(handler: Handler): KChain = KChain(delegate.all(handler))
  fun all(handler: Class<Handler>): KChain = KChain(delegate.all(handler))

  fun path(path: String = "", cb: KContext.() -> Unit): KChain = KChain(delegate.path(path) { KContext(it).cb() })
  fun path(path: String = "", handler: Handler): KChain = KChain(delegate.path(path, handler))
  fun path(path: String = "", handler: Class<Handler>): KChain = KChain(delegate.path(path, handler))

  fun host(hostName: String, cb: KChain.() -> Unit): KChain = KChain(delegate.host(hostName) { KChain(it).cb() })
  fun host(path: String = "", action: Action<in Chain>): KChain = KChain(delegate.host(path, action))
  fun host(path: String = "", action: Class<out Action<in Chain>>): KChain = KChain(delegate.host(path, action))

  fun insert(cb: KChain.() -> Unit): KChain = KChain(delegate.insert { KChain(it).cb() })
  fun insert(action: Action<in Chain>): KChain = KChain(delegate.insert(action))
  fun insert(action: Class<out Action<in Chain>>): KChain = KChain(delegate.insert(action))

  fun register(action: Action<in RegistrySpec>): KChain = KChain(delegate.register(action))
  fun register(registry: Registry, action: Action<in Chain>): KChain = KChain(delegate.register(registry, action))
  fun register(registry: Registry, action: Class<out Action<in Chain>>): KChain = KChain(delegate.register(registry, action))
  fun register(registryAction: Action<in RegistrySpec>, chainAction: Action<in Chain>): KChain = KChain(delegate.register(registryAction, chainAction))
  fun register(registryAction: Action<in RegistrySpec>, chainAction: Class<out Action<in Chain>>): KChain = KChain(delegate.register(registryAction, chainAction))
  fun register(registryAction: RegistrySpec.() -> Unit, cb: KChain.() -> Unit): KChain = KChain(delegate.register({ KRegistrySpec(it).(registryAction)() }, { KChain(it).cb() }))
  fun register(registry: Registry): KChain = KChain(delegate.register(registry))
  fun register(registry: Registry, cb: KChain.() -> Unit): KChain = KChain(delegate.register(registry) { KChain(it).cb() })
  fun register(registryAction: RegistrySpec.() -> Unit): KChain = KChain(delegate.register { KRegistrySpec(it).(registryAction)() })

  fun redirect(code: Int, location: String): KChain = KChain(delegate.redirect(code, location))

  @Suppress("ReplaceGetOrSet")
  fun get(path: String = "", cb: KContext.() -> Unit): KChain = KChain(delegate.get(path) { KContext(it).cb() })
  fun get(path: String = "", handler: Handler): KChain = KChain(delegate.get(path, handler))
  fun get(path: String = "", handler: Class<Handler>): KChain = KChain(delegate.get(path, handler))

  fun put(path: String = "", cb: KContext.() -> Unit): KChain = KChain(delegate.put(path) { KContext(it).cb() })
  fun put(path: String = "", handler: Handler): KChain = KChain(delegate.put(path, handler))
  fun put(path: String = "", handler: Class<Handler>): KChain = KChain(delegate.put(path, handler))

  fun post(path: String = "", cb: KContext.() -> Unit): KChain = KChain(delegate.post(path) { KContext(it).cb() })
  fun post(path: String = "", handler: Handler): KChain = KChain(delegate.post(path, handler))
  fun post(path: String = "", handler: Class<Handler>): KChain = KChain(delegate.post(path, handler))

  fun delete(path: String = "", cb: KContext.() -> Unit): KChain = KChain(delegate.delete(path) { KContext(it).cb() })
  fun delete(path: String = "", handler: Handler): KChain = KChain(delegate.delete(path, handler))
  fun delete(path: String = "", handler: Class<Handler>): KChain = KChain(delegate.delete(path, handler))

  fun options(path: String = "", cb: KContext.() -> Unit): KChain = KChain(delegate.options(path) { KContext(it).cb() })
  fun options(path: String = "", handler: Handler): KChain = KChain(delegate.options(path, handler))
  fun options(path: String = "", handler: Class<Handler>): KChain = KChain(delegate.options(path, handler))

  fun patch(path: String = "", cb: KContext.() -> Unit): KChain = KChain(delegate.patch(path) { KContext(it).cb() })
  fun patch(path: String = "", handler: Handler): KChain = KChain(delegate.patch(path, handler))
  fun patch(path: String = "", handler: Class<Handler>): KChain = KChain(delegate.patch(path, handler))

  fun onlyIf(test: Predicate<in Context>, cb: KContext.() -> Unit): KChain = KChain(delegate.onlyIf(test, KHandlers.from(cb)))
  fun onlyIf(test: Predicate<in Context>, handler: Handler): KChain = KChain(delegate.onlyIf(test, handler))
  fun onlyIf(test: Predicate<in Context>, handler: Class<out Handler>): KChain = KChain(delegate.onlyIf(test, handler))

  fun `when`(test: Predicate<in Context>, action: Action<in Chain>): KChain = KChain(delegate.`when`(test, action))
  fun `when`(test: Predicate<in Context>, action: Class<out Action<in Chain>>): KChain = KChain(delegate.`when`(test, action))
  fun `when`(test: Predicate<in Context>, cb: KChain.() -> Unit): KChain = KChain(delegate.`when`({ ctx: Context -> test.apply(ctx) }) { KChain(it).cb() })
  fun `when`(test: Boolean, cb: KChain.() -> Unit): KChain = KChain(delegate.`when`(test) { KChain(it).cb() })
  fun `when`(test: Boolean, action: Action<in Chain>): KChain = KChain(delegate.`when`(test, action))
  fun `when`(test: Boolean, action: Class<out Action<in Chain>>): KChain = KChain(delegate.`when`(test, action))

  fun notFound(): KChain = KChain(delegate.notFound())
}
