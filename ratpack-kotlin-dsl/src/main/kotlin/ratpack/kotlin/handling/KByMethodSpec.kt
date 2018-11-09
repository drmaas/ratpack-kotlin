package ratpack.kotlin.handling

import ratpack.func.Block
import ratpack.handling.ByMethodSpec
import ratpack.handling.Handler

class KByMethodSpec(val delegate: ByMethodSpec) {
  fun get(cb: KContext.(KContext) -> Unit): KByMethodSpec = get(Handler { val ctx = KContext(it) ; ctx.cb(ctx) })
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

  fun post(cb: KContext.(KContext) -> Unit): KByMethodSpec = post(Handler { val ctx = KContext(it) ; ctx.cb(ctx) })
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

  fun put(cb: KContext.(KContext) -> Unit): KByMethodSpec = put(Handler { val ctx = KContext(it) ; ctx.cb(ctx) })
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

  fun patch(cb: KContext.(KContext) -> Unit): KByMethodSpec = patch(Handler { val ctx = KContext(it) ; ctx.cb(ctx) })
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

  fun options(cb: KContext.(KContext) -> Unit): KByMethodSpec = options(Handler { val ctx = KContext(it) ; ctx.cb(ctx) })
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

  fun delete(cb: KContext.(KContext) -> Unit): KByMethodSpec = delete(Handler { val ctx = KContext(it) ; ctx.cb(ctx) })
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

  fun named(methodName: String, cb: KContext.(KContext) -> Unit): KByMethodSpec = named(methodName, Handler { val ctx = KContext(it) ; ctx.cb(ctx) })
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

}
