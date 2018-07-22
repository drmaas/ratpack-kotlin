package ratpack.kotlin.handling

import ratpack.handling.Handler

object KHandlers {
    inline fun from(crossinline cb: KContext.(KContext) -> Unit) : Handler = { ctx: KContext -> ctx.cb(ctx) } as Handler
}
