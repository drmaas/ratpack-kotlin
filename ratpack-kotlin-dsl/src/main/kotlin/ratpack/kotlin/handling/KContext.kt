package ratpack.kotlin.handling

import ratpack.form.Form
import ratpack.handling.Context
import ratpack.http.TypedData
import ratpack.kotlin.coroutines.async
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class KContext(val delegate: Context) : Context by delegate {
  fun httpDate(date: LocalDateTime) = DateTimeFormatter.RFC_1123_DATE_TIME.format(ZonedDateTime.of(date, ZoneId.of("GMT")))

  inline fun byContent(crossinline cb: KByContentSpec.(KByContentSpec) -> Unit) = delegate.byContent { val s = KByContentSpec(it); s.cb(s) }

  inline fun byMethod (crossinline cb: KByMethodSpec.(KByMethodSpec) -> Unit) = delegate.byMethod { val s = KByMethodSpec(it); s.cb(s) }

  inline fun withBody(crossinline cb: TypedData.(TypedData) -> Unit) = request.body.then { it.cb(it) }

  inline fun withForm(crossinline cb: Form.(Form) -> Unit) = context.parse(Form::class.java).then { it.cb(it) }

  fun send(body: String = "", status: Int = 200) {
    response.status(status)
    if (body == "")
      response.send()
    else
      response.send(body)
  }

  fun ok(body: String = "", status: Int = 200) = send(body, status)
  fun ok(status: Int = 200) = ok("", status)
  fun halt(body: String = "", status: Int = 500) = send(body, status)
  fun halt(status: Int = 500) = halt("", status)

  fun async(cb: suspend () -> Any?) = context.async(cb)
}
