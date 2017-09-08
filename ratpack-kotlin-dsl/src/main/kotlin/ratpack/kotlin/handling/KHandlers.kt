package ratpack.kotlin.handling

import ratpack.handling.Handler

object KHandlers {
    inline fun from(crossinline cb: KContext.(ctx: KContext) -> Unit) : Handler = { ctx: KContext ->  cb(ctx, ctx) } as Handler
}
