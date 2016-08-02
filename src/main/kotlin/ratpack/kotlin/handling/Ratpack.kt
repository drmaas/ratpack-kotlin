package ratpack.kotlin.handling

import ratpack.server.RatpackServer

/*
 * Kotlin DSL top-level function.
 */
fun ratpack(cb: KServerSpec.() -> Unit) = RatpackServer.start { KServerSpec(it).cb() }

