package ratpack.kotlin.handling

import kotlinx.coroutines.ExperimentalCoroutinesApi
import ratpack.handling.Context
import ratpack.handling.Handler
import ratpack.kotlin.coroutines.async

interface KHandler: Handler {
  suspend fun handle(ctx: KContext)
  @OptIn(ExperimentalCoroutinesApi::class)
  override fun handle(ctx: Context) {
    ctx.async {
      handle(KContext(ctx))
    }
  }
}
