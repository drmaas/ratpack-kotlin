package ratpack.kotlin.handling

import ratpack.server.RatpackServer

/*
 * Kotlin DSL top-level function.
 */
inline fun ratpack(crossinline cb: KServerSpec.() -> Unit) = RatpackServer.start { KServerSpec(it).cb() }

