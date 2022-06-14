package ratpack.kotlin.handling

import kotlinx.coroutines.ExperimentalCoroutinesApi
import ratpack.func.Block
import ratpack.handling.ByMethodSpec
import ratpack.handling.Context
import ratpack.handling.Handler
import ratpack.kotlin.coroutines.async

class KByMethodSpec(val delegate: ByMethodSpec) {
  fun get(cb: suspend KContext.(KContext) -> Unit): KByMethodSpec = get(Handler { run(it, cb) })
  fun get(handler: Handler): KByMethodSpec {
    delegate.get(handler)
    return this
  }
  fun get(block: Block): KByMethodSpec {
    delegate.get(block)
    return this
  }
  fun get(clazz: Class<out Handler>): KByMethodSpec {
    delegate.get(clazz)
    return this
  }

  fun post(cb: suspend KContext.(KContext) -> Unit): KByMethodSpec = post(Handler { run(it, cb) })
  fun post(handler: Handler): KByMethodSpec {
    delegate.post(handler)
    return this
  }
  fun post(block: Block): KByMethodSpec {
    delegate.post(block)
    return this
  }
  fun post(clazz: Class<out Handler>): KByMethodSpec {
    delegate.post(clazz)
    return this
  }

  fun put(cb: suspend KContext.(KContext) -> Unit): KByMethodSpec = put(Handler { run(it, cb) })
  fun put(handler: Handler): KByMethodSpec {
     delegate.put(handler)
    return this
  }
  fun put(block: Block): KByMethodSpec {
    delegate.put(block)
    return this
  }
  fun put(clazz: Class<out Handler>): KByMethodSpec {
    delegate.put(clazz)
    return this
  }

  fun patch(cb: suspend suspend  KContext.(KContext) -> Unit): KByMethodSpec = patch(Handler { run(it, cb) })
  fun patch(handler: Handler): KByMethodSpec {
    delegate.patch(handler)
    return this
  }
  fun patch(block: Block): KByMethodSpec {
    delegate.patch(block)
    return this
  }
  fun patch(clazz: Class<out Handler>): KByMethodSpec {
    delegate.patch(clazz)
    return this
  }

  fun options(cb: suspend KContext.(KContext) -> Unit): KByMethodSpec = options(Handler { run(it, cb) })
  fun options(handler: Handler): KByMethodSpec {
    delegate.options(handler)
    return this
  }
  fun options(block: Block): KByMethodSpec {
    delegate.options(block)
    return this
  }
  fun options(clazz: Class<out Handler>): KByMethodSpec {
    delegate.options(clazz)
    return this
  }

  fun delete(cb: suspend KContext.(KContext) -> Unit): KByMethodSpec = delete(Handler { run(it, cb) })
  fun delete(handler: Handler): KByMethodSpec {
    delegate.delete(handler)
    return this
  }
  fun delete(block: Block): KByMethodSpec {
    delegate.delete(block)
    return this
  }
  fun delete(clazz: Class<out Handler>): KByMethodSpec {
    delegate.delete(clazz)
    return this
  }

  fun named(methodName: String, cb: suspend KContext.(KContext) -> Unit): KByMethodSpec = named(methodName, Handler { run(it, cb) })
  fun named(methodName: String, handler: Handler): KByMethodSpec {
    delegate.named(methodName, handler)
    return this
  }
  fun named(methodName: String, block: Block): KByMethodSpec {
    delegate.named(methodName, block)
    return this
  }
  fun named(methodName: String, clazz: Class<out Handler>): KByMethodSpec {
    delegate.named(methodName, clazz)
    return this
  }

  @OptIn(ExperimentalCoroutinesApi::class)
  private inline fun run(context: Context, crossinline cb: suspend KContext.(KContext) -> Unit) {
    val ctx = KContext(context)
    ctx.async {
      ctx.cb(ctx)
    }
  }

}
