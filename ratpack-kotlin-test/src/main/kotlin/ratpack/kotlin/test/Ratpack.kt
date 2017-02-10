package ratpack.kotlin.test

import ratpack.kotlin.handling.KServerSpec
import ratpack.server.RatpackServer

fun ratpack(cb: KServerSpec.() -> Unit) = RatpackKotlinApplicationUnderTest(RatpackServer.start { KServerSpec(it).cb() })
