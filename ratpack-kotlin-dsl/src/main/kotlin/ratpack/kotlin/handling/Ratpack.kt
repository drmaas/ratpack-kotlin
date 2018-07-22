package ratpack.kotlin.handling

import ratpack.server.RatpackServer

/*
 * Kotlin DSL top-level function.
 */
inline fun ratpack(crossinline cb: KServerSpec.(KServerSpec) -> Unit) = RatpackServer.start { val s = KServerSpec(it); s.cb(s) }
