package ratpack.kotlin.coroutines.client

import com.google.common.reflect.TypeToken
import kotlinx.coroutines.Deferred
import org.slf4j.MDC
import ratpack.exec.ExecSpec
import ratpack.exec.Execution
import ratpack.func.Action
import ratpack.http.client.HttpClient
import ratpack.http.client.ReceivedResponse
import ratpack.http.client.RequestSpec
import ratpack.kotlin.coroutines.defer
import java.net.URI
import java.util.Optional

/**
 * Wrap the client in a new layer that returns a [Deferred] instead of a [ratpack.exec.Promise].
 *
 * Each call, if executed will copy MDC and registry entries from the parent execution.
 *
 * Each call will also be executed in a forked compute thread for easy parallelization.
 *
 * All functions are eager. To make then lazy, and cache their results, use
 * [ratpack.kotlin.coroutines.lazyDefer] or [ratpack.kotlin.coroutines.lazyDeferIf]
 */
class CioHttpClient(val client: HttpClient) {

  private val execSpec: (ExecSpec) -> Unit = { spec ->
    val parentMDC = MDC.getCopyOfContextMap()
    val executionEntries = getExecutionEntries()
    spec.register { r -> executionEntries.forEach { r.add(it) } }
    spec.onStart {
      if (parentMDC != null) {
        MDC.setContextMap(parentMDC)
      }
    }
  }

  private fun getExecutionEntries(): Iterable<Any> {
    return getExecution().map { e ->
      e.getAll(TypeToken.of(Any::class.java))
    }.orElse(emptyList())
  }

  private fun getExecution(): Optional<Execution> {
    var p: Execution? = null
    try {
      p = Execution.current()
    } catch (e: IllegalStateException) {
    }
    return Optional.ofNullable(p)
  }

  fun get(uri: URI, action: Action<in RequestSpec>): Deferred<ReceivedResponse> {
    return client.request(uri, action.prepend { it.get() }).defer(true, execSpec)
  }

  fun get(uri: String, action: (RequestSpec) -> Unit): Deferred<ReceivedResponse> {
    return get(URI.create(uri), Action { action(it) })
  }

  fun get(uri: String): Deferred<ReceivedResponse> {
    val action: (RequestSpec) -> Unit = {}
    return get(uri, action)
  }

  fun put(uri: URI, action: Action<in RequestSpec>): Deferred<ReceivedResponse> {
    return client.request(uri, action.prepend { it.put() }).defer(true, execSpec)
  }

  fun put(uri: URI): Deferred<ReceivedResponse> {
    return put(uri, Action.noop())
  }

  fun put(uri: String, action: (RequestSpec) -> Unit): Deferred<ReceivedResponse> {
    return put(URI.create(uri), Action { action(it) })
  }

  fun put(uri: String): Deferred<ReceivedResponse> {
    val action: (RequestSpec) -> Unit = {}
    return put(uri, action)
  }

  fun post(uri: String, action: (RequestSpec) -> Unit): Deferred<ReceivedResponse> {
    return client.post(URI.create(uri), action).defer(true, execSpec)
  }

  fun post(uri: String): Deferred<ReceivedResponse> {
    val action: (RequestSpec) -> Unit = {}
    return post(uri, action)
  }

  fun delete(uri: URI, action: Action<in RequestSpec>): Deferred<ReceivedResponse> {
    return client.request(uri, action.prepend { it.delete() }).defer(true, execSpec)
  }

  fun delete(uri: URI): Deferred<ReceivedResponse> {
    return delete(uri, Action.noop())
  }

  fun delete(uri: String, action: (RequestSpec) -> Unit): Deferred<ReceivedResponse> {
    return delete(URI.create(uri), Action { action(it) })
  }

  fun delete(uri: String): Deferred<ReceivedResponse> {
    val action: (RequestSpec) -> Unit = {}
    return delete(uri, action)
  }

  fun patch(uri: URI, action: Action<in RequestSpec>): Deferred<ReceivedResponse> {
    return client.request(uri, action.prepend { it.patch() }).defer(true, execSpec)
  }

  fun patch(uri: URI): Deferred<ReceivedResponse> {
    return patch(uri, Action.noop())
  }

  fun patch(uri: String, action: (RequestSpec) -> Unit): Deferred<ReceivedResponse> {
    return patch(URI.create(uri), Action { action(it) })
  }

  fun patch(uri: String): Deferred<ReceivedResponse> {
    val action: (RequestSpec) -> Unit = {}
    return patch(uri, action)
  }

  fun options(uri: URI, action: Action<in RequestSpec>): Deferred<ReceivedResponse> {
    return client.request(uri, action.prepend { it.options() }).defer(true, execSpec)
  }

  fun options(uri: URI): Deferred<ReceivedResponse> {
    return options(uri, Action.noop())
  }

  fun options(uri: String, action: (RequestSpec) -> Unit): Deferred<ReceivedResponse> {
    return options(URI.create(uri), Action { action(it) })
  }

  fun options(uri: String): Deferred<ReceivedResponse> {
    val action: (RequestSpec) -> Unit = {}
    return options(uri, action)
  }

  fun head(uri: URI, action: Action<in RequestSpec>): Deferred<ReceivedResponse> {
    return client.request(uri, action.prepend { it.head() }).defer(true, execSpec)
  }

  fun head(uri: URI): Deferred<ReceivedResponse> {
    return head(uri, Action.noop())
  }

  fun head(uri: String, action: (RequestSpec) -> Unit): Deferred<ReceivedResponse> {
    return head(URI.create(uri), Action { action(it) })
  }

  fun head(uri: String): Deferred<ReceivedResponse> {
    val action: (RequestSpec) -> Unit = {}
    return head(uri, action)
  }

}
