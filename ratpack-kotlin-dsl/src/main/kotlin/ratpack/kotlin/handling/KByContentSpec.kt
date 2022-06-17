package ratpack.kotlin.handling

import kotlinx.coroutines.ExperimentalCoroutinesApi
import ratpack.func.Block
import ratpack.handling.ByContentSpec
import ratpack.handling.Chain
import ratpack.handling.Context
import ratpack.handling.Handler
import ratpack.kotlin.coroutines.async

class KByContentSpec(val delegate: ByContentSpec) {

  fun type (mimeType: String, handler: suspend KContext.(KContext) -> Unit) = type(mimeType, Handler { run(it, handler) })
  fun type(mimeType: String, handler: Handler) = delegate.type(mimeType, handler)
  fun type(mimeType: String, block: Block) = delegate.type(mimeType, block)
  fun type(mimeType: String, clazz: Class<out Handler>) = delegate.type(mimeType, clazz)

  fun plainText (handler: suspend KContext.(KContext) -> Unit) = plainText(Handler { run(it, handler) })
  fun plainText(handler: Handler) = delegate.plainText(handler)
  fun plainText(block: Block) = delegate.plainText(block)
  fun plainText(clazz: Class<out Handler>) = delegate.plainText(clazz)

  fun html (handler: suspend KContext.(KContext) -> Unit) = html(Handler { run(it, handler) })
  fun html(handler: Handler) = delegate.html(handler)
  fun html(block: Block) = delegate.html(block)
  fun html(clazz: Class<out Handler>) = delegate.html(clazz)

  fun json (handler: suspend KContext.(KContext) -> Unit) = json(Handler { run(it, handler) })
  fun json(handler: Handler) = delegate.json(handler)
  fun json(block: Block) = delegate.json(block)
  fun json(clazz: Class<out Handler>) = delegate.json(clazz)

  fun xml (handler: suspend KContext.(KContext) -> Unit) = xml(Handler { run(it, handler) })
  fun xml(handler: Handler) = delegate.xml(handler)
  fun xml(block: Block) = delegate.xml(block)
  fun xml(clazz: Class<out Handler>) = delegate.xml(clazz)

  fun noMatch (handler: suspend KContext.(KContext) -> Unit) = noMatch(Handler { run(it, handler) })
  fun noMatch(handler: Handler) = delegate.noMatch(handler)
  fun noMatch(block: Block) = delegate.noMatch(block)
  fun noMatch(clazz: Class<out Handler>) = delegate.noMatch(clazz)

  fun unspecified (handler: suspend KContext.(KContext) -> Unit) = unspecified(Handler { run(it, handler) })
  fun unspecified(handler: Handler) = delegate.unspecified(handler)
  fun unspecified(block: Block) = delegate.unspecified(block)
  fun unspecified(clazz: Class<out Handler>) = delegate.unspecified(clazz)

  @OptIn(ExperimentalCoroutinesApi::class)
  private inline fun run(context: Context, crossinline cb: suspend KContext.(KContext) -> Unit) {
    val ctx = KContext(context)
    ctx.async {
      ctx.cb(ctx)
    }
  }

}
