package ratpack.kotlin.handling

import ratpack.handling.Context
import ratpack.handling.Handler

interface KHandler: Handler {
  fun handle(ctx: KContext)
  override fun handle(ctx: Context) {
    handle(KContext(ctx))
  }
}
