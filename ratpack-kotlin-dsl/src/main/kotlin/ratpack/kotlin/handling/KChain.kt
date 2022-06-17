package ratpack.kotlin.handling

import kotlinx.coroutines.ExperimentalCoroutinesApi
import ratpack.file.FileHandlerSpec
import ratpack.func.Action
import ratpack.func.Predicate
import ratpack.handling.Chain
import ratpack.handling.Context
import ratpack.handling.Handler
import ratpack.kotlin.coroutines.async
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

  inline fun files(crossinline cb: FileHandlerSpec.(FileHandlerSpec) -> Unit): KChain {
    delegate.files { it.cb(it) }
    return this
  }
  fun files(): KChain {
    delegate.files()
    return this
  }
  fun files(action: Action<in FileHandlerSpec>): KChain {
    delegate.files(action)
    return this
  }

  inline fun fileSystem(path: String = "", crossinline cb: KChain.(KChain) -> Unit): KChain {
    delegate.fileSystem(path) { run(it, cb) }
    return this
  }
  fun fileSystem(path: String = "", action: Action<in Chain>): KChain{
    delegate.fileSystem(path, action)
    return this
  }
  fun fileSystem(path: String = "", action: Class<out Action<in Chain>>): KChain {
    delegate.fileSystem(path, action)
    return this
  }

  inline fun prefix(path: String = "", crossinline cb: KChain.(KChain) -> Unit): KChain {
    delegate.prefix(path) { run(it, cb) }
    return this
  }
  fun prefix(path: String = "", action: Action<in Chain>): KChain {
    delegate.prefix(path, action)
    return this
  }
  fun prefix(path: String = "", action: Class<out Action<in Chain>>): KChain {
    delegate.prefix(path, action)
    return this
  }

  inline fun all(crossinline cb: suspend KContext.(KContext) -> Unit): KChain {
    delegate.all { run(it, cb)}
    return this
  }
  fun all(handler: Handler): KChain {
    delegate.all(handler)
    return this
  }
  fun all(handler: Class<out Handler>): KChain {
    delegate.all(handler)
    return this
  }

  inline fun path(path: String = "", crossinline cb: suspend KContext.(KContext) -> Unit): KChain {
    delegate.path(path) { run(it, cb) }
    return this
  }
  fun path(path: String = "", handler: Handler): KChain {
    delegate.path(path, handler)
    return this
  }
  fun path(path: String = "", handler: Class<out Handler>): KChain {
    delegate.path(path, handler)
    return this
  }

  inline fun host(hostName: String, crossinline cb: KChain.(KChain) -> Unit): KChain {
    delegate.host(hostName) { run(it, cb) }
    return this
  }
  fun host(path: String = "", action: Action<in Chain>): KChain {
    delegate.host(path, action)
    return this
  }
  fun host(path: String = "", action: Class<out Action<in Chain>>): KChain {
    delegate.host(path, action)
    return this
  }

  inline fun insert(crossinline cb: KChain.(KChain) -> Unit): KChain {
    delegate.insert { run(it, cb) }
    return this
  }
  fun insert(action: Action<in Chain>): KChain {
    delegate.insert(action)
    return this
  }
  fun insert(action: Class<out Action<in Chain>>): KChain {
    delegate.insert(action)
    return this
  }

  fun register(action: Action<in RegistrySpec>): KChain {
    delegate.register(action)
    return this
  }
  fun register(registry: Registry, action: Action<in Chain>): KChain {
    delegate.register(registry, action)
    return this
  }
  fun register(registry: Registry, action: Class<out Action<in Chain>>): KChain {
    delegate.register(registry, action)
    return this
  }
  fun register(registryAction: Action<in RegistrySpec>, chainAction: Action<in Chain>): KChain {
    delegate.register(registryAction, chainAction)
    return this
  }
  fun register(registryAction: Action<in RegistrySpec>, chainAction: Class<out Action<in Chain>>): KChain {
    delegate.register(registryAction, chainAction)
    return this
  }
  inline fun register(crossinline registryAction: KRegistrySpec.(KRegistrySpec) -> Unit, crossinline cb: KChain.(c: KChain) -> Unit): KChain {
    delegate.register({ val r = KRegistrySpec(it); r.registryAction(r) }, { run(it, cb) })
    return this
  }
  fun register(registry: Registry): KChain {
    delegate.register(registry)
    return this
  }
  inline fun register(registry: Registry, crossinline cb: KChain.(KChain) -> Unit): KChain {
    delegate.register(registry) { run(it, cb) }
    return this
  }
  inline fun register(crossinline registryAction: KRegistrySpec.(KRegistrySpec) -> Unit): KChain {
    delegate.register { val r = KRegistrySpec(it); r.registryAction(r) }
    return this
  }

  fun redirect(code: Int, location: String): KChain {
    delegate.redirect(code, location)
    return this
  }

  @Suppress("ReplaceGetOrSet")
  inline fun get(path: String = "", crossinline cb: suspend KContext.(KContext) -> Unit): KChain {
    delegate.get(path) { run(it, cb) }
    return this
  }
  fun get(path: String = "", handler: Handler): KChain {
    delegate.get(path, handler)
    return this
  }
  fun get(path: String = "", handler: Class<out Handler>): KChain {
    delegate.get(path, handler)
    return this
  }

  inline fun put(path: String = "", crossinline cb: suspend KContext.(KContext) -> Unit): KChain {
    delegate.put(path) { run(it, cb) }
    return this
  }
  fun put(path: String = "", handler: Handler): KChain {
    delegate.put(path, handler)
    return this
  }
  fun put(path: String = "", handler: Class<out Handler>): KChain {
    delegate.put(path, handler)
    return this
  }

  inline fun post(path: String = "", crossinline cb: suspend KContext.(KContext) -> Unit): KChain {
    delegate.post(path) { run(it, cb) }
    return this
  }
  fun post(path: String = "", handler: Handler): KChain {
    delegate.post(path, handler)
    return this
  }
  fun post(path: String = "", handler: Class<out Handler>): KChain {
    delegate.post(path, handler)
    return this
  }

  inline fun delete(path: String = "", crossinline cb: suspend KContext.(KContext) -> Unit): KChain {
    delegate.delete(path) { run(it, cb) }
    return this
  }
  fun delete(path: String = "", handler: Handler): KChain {
    delegate.delete(path, handler)
    return this
  }
  fun delete(path: String = "", handler: Class<out Handler>): KChain {
    delegate.delete(path, handler)
    return this
  }

  inline fun options(path: String = "", crossinline cb: suspend KContext.(KContext) -> Unit): KChain {
    delegate.options(path) { run(it, cb) }
    return this
  }
  fun options(path: String = "", handler: Handler): KChain {
    delegate.options(path, handler)
    return this
  }
  fun options(path: String = "", handler: Class<out Handler>): KChain {
    delegate.options(path, handler)
    return this
  }

  inline fun patch(path: String = "", crossinline cb: suspend KContext.(KContext) -> Unit): KChain {
    delegate.patch(path) { run(it, cb) }
    return this
  }
  fun patch(path: String = "", handler: Handler): KChain {
    delegate.patch(path, handler)
    return this
  }
  fun patch(path: String = "", handler: Class<out Handler>): KChain {
    delegate.patch(path, handler)
    return this
  }

  fun onlyIf(test: Predicate<in Context>, cb: suspend KContext.(KContext) -> Unit): KChain {
    delegate.onlyIf({ ctx: Context -> test.apply(ctx) }, { run(it, cb) })
    return this
  }
  fun onlyIf(test: Predicate<in Context>, handler: Handler): KChain {
    delegate.onlyIf(test, handler)
    return this
  }
  fun onlyIf(test: Predicate<in Context>, handler: Class<out Handler>): KChain {
    delegate.onlyIf(test, handler)
    return this
  }

  fun `when`(test: Predicate<in Context>, action: Action<in Chain>): KChain {
    delegate.`when`(test, action)
    return this
  }
  fun `when`(test: Predicate<in Context>, action: Class<out Action<in Chain>>): KChain {
    delegate.`when`(test, action)
    return this
  }
  inline fun `when`(test: Predicate<in Context>, crossinline cb: KChain.(KChain) -> Unit): KChain {
    delegate.`when`({ ctx: Context -> test.apply(ctx) }) { run(it, cb) }
    return this
  }
  inline fun `when`(test: Boolean, crossinline cb: KChain.(KChain) -> Unit): KChain {
    delegate.`when`(test) { run(it, cb) }
    return this
  }
  fun `when`(test: Boolean, action: Action<in Chain>): KChain {
    delegate.`when`(test, action)
    return this
  }
  fun `when`(test: Boolean, action: Class<out Action<in Chain>>): KChain {
    delegate.`when`(test, action)
    return this
  }

  fun notFound(): KChain {
    delegate.notFound()
    return this
  }

  @OptIn(ExperimentalCoroutinesApi::class)
  inline fun run(context: Context, crossinline cb: suspend KContext.(KContext) -> Unit) {
    val ctx = KContext(context)
    ctx.async {
      ctx.cb(ctx)
    }
  }

  inline fun run(chain: Chain, crossinline cb: KChain.(KChain) -> Unit) {
    val c = KChain(chain)
    c.cb(c)
  }

}
