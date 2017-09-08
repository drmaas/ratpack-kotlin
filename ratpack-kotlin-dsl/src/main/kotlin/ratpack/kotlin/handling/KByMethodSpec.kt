package ratpack.kotlin.handling

import ratpack.func.Block
import ratpack.handling.ByMethodSpec
import ratpack.handling.Handler

class KByMethodSpec(val delegate: ByMethodSpec) {
  fun get(cb: KContext.(ctx: KContext) -> Unit): KByMethodSpec = get(Handler { val ctx = KContext(it) ; ctx.cb(ctx) })
  fun get(handler: Handler): KByMethodSpec = KByMethodSpec(delegate.get(handler))
  fun get(block: Block): KByMethodSpec = KByMethodSpec(delegate.get(block))
  fun get(clazz: Class<out Handler>): KByMethodSpec = KByMethodSpec(delegate.get(clazz))

  fun post(cb: KContext.(ctx: KContext) -> Unit): KByMethodSpec = post(Handler { val ctx = KContext(it) ; ctx.cb(ctx) })
  fun post(handler: Handler): KByMethodSpec = KByMethodSpec(delegate.post(handler))
  fun post(block: Block): KByMethodSpec = KByMethodSpec(delegate.post(block))
  fun post(clazz: Class<out Handler>): KByMethodSpec = KByMethodSpec(delegate.post(clazz))

  fun put(cb: KContext.(ctx: KContext) -> Unit): KByMethodSpec = put(Handler { val ctx = KContext(it) ; ctx.cb(ctx) })
  fun put(handler: Handler): KByMethodSpec = KByMethodSpec(delegate.put(handler))
  fun put(block: Block): KByMethodSpec = KByMethodSpec(delegate.put(block))
  fun put(clazz: Class<out Handler>): KByMethodSpec = KByMethodSpec(delegate.put(clazz))

  fun patch(cb: KContext.(ctx: KContext) -> Unit): KByMethodSpec = patch(Handler { val ctx = KContext(it) ; ctx.cb(ctx) })
  fun patch(handler: Handler): KByMethodSpec = KByMethodSpec(delegate.patch(handler))
  fun patch(block: Block): KByMethodSpec = KByMethodSpec(delegate.patch(block))
  fun patch(clazz: Class<out Handler>): KByMethodSpec = KByMethodSpec(delegate.patch(clazz))

  fun options(cb: KContext.(ctx: KContext) -> Unit): KByMethodSpec = options(Handler { val ctx = KContext(it) ; ctx.cb(ctx) })
  fun options(handler: Handler): KByMethodSpec = KByMethodSpec(delegate.options(handler))
  fun options(block: Block): KByMethodSpec = KByMethodSpec(delegate.options(block))
  fun options(clazz: Class<out Handler>): KByMethodSpec = KByMethodSpec(delegate.options(clazz))

  fun delete(cb: KContext.(ctx: KContext) -> Unit): KByMethodSpec = delete(Handler { val ctx = KContext(it) ; ctx.cb(ctx) })
  fun delete(handler: Handler): KByMethodSpec = KByMethodSpec(delegate.delete(handler))
  fun delete(block: Block): KByMethodSpec = KByMethodSpec(delegate.delete(block))
  fun delete(clazz: Class<out Handler>): KByMethodSpec = KByMethodSpec(delegate.delete(clazz))

  fun named(methodName: String, cb: KContext.(ctx: KContext) -> Unit): KByMethodSpec = named(methodName, Handler { val ctx = KContext(it) ; ctx.cb(ctx) })
  fun named(methodName: String, handler: Handler): KByMethodSpec = KByMethodSpec(delegate.named(methodName, handler))
  fun named(methodName: String, block: Block): KByMethodSpec = KByMethodSpec(delegate.named(methodName, block))
  fun named(methodName: String, clazz: Class<out Handler>): KByMethodSpec = KByMethodSpec(delegate.named(methodName, clazz))

}
