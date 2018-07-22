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

  inline fun chain(crossinline cb: KChain.(KChain) -> Unit): Handler = delegate.chain { val c = KChain(it); c.cb(c) }
  fun chain(action: Class<out Action<in Chain>>): Handler = delegate.chain(action)

  inline fun files(crossinline cb: FileHandlerSpec.(FileHandlerSpec) -> Unit): KChain = KChain(delegate.files { it.cb(it) })
  fun files(): KChain = KChain(delegate.files())
  fun files(action: Action<in FileHandlerSpec>): KChain = KChain(delegate.files(action))

  inline fun fileSystem(path: String = "", crossinline cb: KChain.(KChain) -> Unit): KChain = KChain(delegate.fileSystem(path) { val c = KChain(it); c.cb(c) })
  fun fileSystem(path: String = "", action: Action<in Chain>): KChain = KChain(delegate.fileSystem(path, action))
  fun fileSystem(path: String = "", action: Class<out Action<in Chain>>): KChain = KChain(delegate.fileSystem(path, action))

  inline fun prefix(path: String = "", crossinline cb: KChain.(KChain) -> Unit): KChain = KChain(delegate.prefix(path) { val c = KChain(it); c.cb(c) })
  fun prefix(path: String = "", action: Action<in Chain>): KChain = KChain(delegate.prefix(path, action))
  fun prefix(path: String = "", action: Class<out Action<in Chain>>): KChain = KChain(delegate.prefix(path, action))

  inline fun all(crossinline cb: KContext.(KContext) -> Unit): KChain = KChain(delegate.all { val c = KContext(it); c.cb(c) })
  fun all(handler: Handler): KChain = KChain(delegate.all(handler))
  fun all(handler: Class<out Handler>): KChain = KChain(delegate.all(handler))

  inline fun path(path: String = "", crossinline cb: KContext.(KContext) -> Unit): KChain = KChain(delegate.path(path) { val ctx = KContext(it); ctx.cb(ctx) })
  fun path(path: String = "", handler: Handler): KChain = KChain(delegate.path(path, handler))
  fun path(path: String = "", handler: Class<out Handler>): KChain = KChain(delegate.path(path, handler))

  inline fun host(hostName: String, crossinline cb: KChain.(KChain) -> Unit): KChain = KChain(delegate.host(hostName) { val c = KChain(it); c.cb(c) })
  fun host(path: String = "", action: Action<in Chain>): KChain = KChain(delegate.host(path, action))
  fun host(path: String = "", action: Class<out Action<in Chain>>): KChain = KChain(delegate.host(path, action))

  inline fun insert(crossinline cb: KChain.(KChain) -> Unit): KChain = KChain(delegate.insert { val c = KChain(it); c.cb(c) })
  fun insert(action: Action<in Chain>): KChain = KChain(delegate.insert(action))
  fun insert(action: Class<out Action<in Chain>>): KChain = KChain(delegate.insert(action))

  fun register(action: Action<in RegistrySpec>): KChain = KChain(delegate.register(action))
  fun register(registry: Registry, action: Action<in Chain>): KChain = KChain(delegate.register(registry, action))
  fun register(registry: Registry, action: Class<out Action<in Chain>>): KChain = KChain(delegate.register(registry, action))
  fun register(registryAction: Action<in RegistrySpec>, chainAction: Action<in Chain>): KChain = KChain(delegate.register(registryAction, chainAction))
  fun register(registryAction: Action<in RegistrySpec>, chainAction: Class<out Action<in Chain>>): KChain = KChain(delegate.register(registryAction, chainAction))
  inline fun register(crossinline registryAction: KRegistrySpec.(KRegistrySpec) -> Unit, crossinline cb: KChain.(c: KChain) -> Unit): KChain = KChain(delegate.register({ val r = KRegistrySpec(it); r.registryAction(r) }, { val c = KChain(it); c.cb(c) }))
  fun register(registry: Registry): KChain = KChain(delegate.register(registry))
  inline fun register(registry: Registry, crossinline cb: KChain.(KChain) -> Unit): KChain = KChain(delegate.register(registry) { val c = KChain(it); c.cb(c) })
  inline fun register(crossinline registryAction: KRegistrySpec.(KRegistrySpec) -> Unit): KChain = KChain(delegate.register { val r = KRegistrySpec(it); r.registryAction(r) })

  fun redirect(code: Int, location: String): KChain = KChain(delegate.redirect(code, location))

  @Suppress("ReplaceGetOrSet")
  inline fun get(path: String = "", crossinline cb: KContext.(KContext) -> Unit): KChain = KChain(delegate.get(path) { val c = KContext(it); c.cb(c) })
  fun get(path: String = "", handler: Handler): KChain = KChain(delegate.get(path, handler))
  fun get(path: String = "", handler: Class<out Handler>): KChain = KChain(delegate.get(path, handler))

  inline fun put(path: String = "", crossinline cb: KContext.(KContext) -> Unit): KChain = KChain(delegate.put(path) { val c = KContext(it); c.cb(c) })
  fun put(path: String = "", handler: Handler): KChain = KChain(delegate.put(path, handler))
  fun put(path: String = "", handler: Class<out Handler>): KChain = KChain(delegate.put(path, handler))

  inline fun post(path: String = "", crossinline cb: KContext.(KContext) -> Unit): KChain = KChain(delegate.post(path) { val c = KContext(it); c.cb(c) })
  fun post(path: String = "", handler: Handler): KChain = KChain(delegate.post(path, handler))
  fun post(path: String = "", handler: Class<out Handler>): KChain = KChain(delegate.post(path, handler))

  inline fun delete(path: String = "", crossinline cb: KContext.(KContext) -> Unit): KChain = KChain(delegate.delete(path) { val c = KContext(it); c.cb(c) })
  fun delete(path: String = "", handler: Handler): KChain = KChain(delegate.delete(path, handler))
  fun delete(path: String = "", handler: Class<out Handler>): KChain = KChain(delegate.delete(path, handler))

  inline fun options(path: String = "", crossinline cb: KContext.(KContext) -> Unit): KChain = KChain(delegate.options(path) { val c = KContext(it); c.cb(c) })
  fun options(path: String = "", handler: Handler): KChain = KChain(delegate.options(path, handler))
  fun options(path: String = "", handler: Class<out Handler>): KChain = KChain(delegate.options(path, handler))

  inline fun patch(path: String = "", crossinline cb: KContext.(KContext) -> Unit): KChain = KChain(delegate.patch(path) { val c = KContext(it); c.cb(c) })
  fun patch(path: String = "", handler: Handler): KChain = KChain(delegate.patch(path, handler))
  fun patch(path: String = "", handler: Class<out Handler>): KChain = KChain(delegate.patch(path, handler))

  fun onlyIf(test: Predicate<in Context>, cb: KContext.(KContext) -> Unit): KChain = KChain(delegate.onlyIf(test, KHandlers.from(cb)))
  fun onlyIf(test: Predicate<in Context>, handler: Handler): KChain = KChain(delegate.onlyIf(test, handler))
  fun onlyIf(test: Predicate<in Context>, handler: Class<out Handler>): KChain = KChain(delegate.onlyIf(test, handler))

  fun `when`(test: Predicate<in Context>, action: Action<in Chain>): KChain = KChain(delegate.`when`(test, action))
  fun `when`(test: Predicate<in Context>, action: Class<out Action<in Chain>>): KChain = KChain(delegate.`when`(test, action))
  inline fun `when`(test: Predicate<in Context>, crossinline cb: KChain.(KChain) -> Unit): KChain = KChain(delegate.`when`({ ctx: Context -> test.apply(ctx) }) { val c = KChain(it); c.cb(c) })
  inline fun `when`(test: Boolean, crossinline cb: KChain.(KChain) -> Unit): KChain = KChain(delegate.`when`(test) { val c = KChain(it); c.cb(c) })
  fun `when`(test: Boolean, action: Action<in Chain>): KChain = KChain(delegate.`when`(test, action))
  fun `when`(test: Boolean, action: Class<out Action<in Chain>>): KChain = KChain(delegate.`when`(test, action))

  fun notFound(): KChain = KChain(delegate.notFound())
}
