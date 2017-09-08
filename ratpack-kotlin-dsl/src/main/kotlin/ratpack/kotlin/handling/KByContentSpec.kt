package ratpack.kotlin.handling

import ratpack.func.Block
import ratpack.handling.ByContentSpec
import ratpack.handling.Handler

class KByContentSpec(val delegate: ByContentSpec) {

  fun type (mimeType: String, handler: KContext.(ctx: KContext) -> Unit) = type(mimeType, Handler { val ctx = KContext(it); ctx.handler(ctx) })
  fun type(mimeType: String, handler: Handler) = delegate.type(mimeType, handler)
  fun type(mimeType: String, block: Block) = delegate.type(mimeType, block)
  fun type(mimeType: String, clazz: Class<out Handler>) = delegate.type(mimeType, clazz)

  fun plainText (handler: KContext.(ctx: KContext) -> Unit) = plainText(Handler { val ctx = KContext(it); ctx.handler(ctx) })
  fun plainText(handler: Handler) = delegate.plainText(handler)
  fun plainText(block: Block) = delegate.plainText(block)
  fun plainText(clazz: Class<out Handler>) = delegate.plainText(clazz)

  fun html (handler: KContext.(ctx: KContext) -> Unit) = html(Handler { val ctx = KContext(it); ctx.handler(ctx) })
  fun html(handler: Handler) = delegate.html(handler)
  fun html(block: Block) = delegate.html(block)
  fun html(clazz: Class<out Handler>) = delegate.html(clazz)

  fun json (handler: KContext.(ctx: KContext) -> Unit) = json(Handler { val ctx = KContext(it); ctx.handler(ctx) })
  fun json(handler: Handler) = delegate.json(handler)
  fun json(block: Block) = delegate.json(block)
  fun json(clazz: Class<out Handler>) = delegate.json(clazz)

  fun xml (handler: KContext.(ctx: KContext) -> Unit) = xml(Handler { val ctx = KContext(it); ctx.handler(ctx) })
  fun xml(handler: Handler) = delegate.xml(handler)
  fun xml(block: Block) = delegate.xml(block)
  fun xml(clazz: Class<out Handler>) = delegate.xml(clazz)

  fun noMatch (handler: KContext.(ctx: KContext) -> Unit) = noMatch(Handler { val ctx = KContext(it); ctx.handler(ctx) })
  fun noMatch(handler: Handler) = delegate.noMatch(handler)
  fun noMatch(block: Block) = delegate.noMatch(block)
  fun noMatch(clazz: Class<out Handler>) = delegate.noMatch(clazz)

  fun unspecified (handler: KContext.(ctx: KContext) -> Unit) = unspecified(Handler { val ctx = KContext(it); ctx.handler(ctx) })
  fun unspecified(handler: Handler) = delegate.unspecified(handler)
  fun unspecified(block: Block) = delegate.unspecified(block)
  fun unspecified(clazz: Class<out Handler>) = delegate.unspecified(clazz)


}
