package ratpack.kotlin.client

import ratpack.exec.Promise
import ratpack.func.Action
import ratpack.http.client.HttpClient
import ratpack.http.client.ReceivedResponse
import ratpack.http.client.RequestSpec
import java.net.URI

fun HttpClient.get(uri: String, action: (RequestSpec) -> Unit): Promise<ReceivedResponse> {
  return get(URI.create(uri), action)
}

fun HttpClient.get(uri: String): Promise<ReceivedResponse> {
  val action: (RequestSpec) -> Unit = {}
  return get(uri, action)
}

fun HttpClient.put(uri: URI, action: Action<in RequestSpec>): Promise<ReceivedResponse> {
  return request(uri, action.prepend { it.put() })
}

fun HttpClient.put(uri: URI): Promise<ReceivedResponse> {
  return put(uri, Action.noop())
}

fun HttpClient.put(uri: String, action: (RequestSpec) -> Unit): Promise<ReceivedResponse> {
  return put(URI.create(uri), Action { action(it) })
}

fun HttpClient.put(uri: String): Promise<ReceivedResponse> {
  val action: (RequestSpec) -> Unit = {}
  return put(uri, action)
}

fun HttpClient.post(uri: String, action: (RequestSpec) -> Unit): Promise<ReceivedResponse> {
  return post(URI.create(uri), action)
}

fun HttpClient.post(uri: String): Promise<ReceivedResponse> {
  val action: (RequestSpec) -> Unit = {}
  return post(uri, action)
}

fun HttpClient.delete(uri: URI, action: Action<in RequestSpec>): Promise<ReceivedResponse> {
  return request(uri, action.prepend { it.delete() })
}

fun HttpClient.delete(uri: URI): Promise<ReceivedResponse> {
  return delete(uri, Action.noop())
}

fun HttpClient.delete(uri: String, action: (RequestSpec) -> Unit): Promise<ReceivedResponse> {
  return delete(URI.create(uri), Action { action(it) })
}

fun HttpClient.delete(uri: String): Promise<ReceivedResponse> {
  val action: (RequestSpec) -> Unit = {}
  return delete(uri, action)
}

fun HttpClient.patch(uri: URI, action: Action<in RequestSpec>): Promise<ReceivedResponse> {
  return request(uri, action.prepend { it.patch() })
}

fun HttpClient.patch(uri: URI): Promise<ReceivedResponse> {
  return patch(uri, Action.noop())
}

fun HttpClient.patch(uri: String, action: (RequestSpec) -> Unit): Promise<ReceivedResponse> {
  return patch(URI.create(uri), Action { action(it) })
}

fun HttpClient.patch(uri: String): Promise<ReceivedResponse> {
  val action: (RequestSpec) -> Unit = {}
  return patch(uri, action)
}

fun HttpClient.options(uri: URI, action: Action<in RequestSpec>): Promise<ReceivedResponse> {
  return request(uri, action.prepend { it.options() })
}

fun HttpClient.options(uri: URI): Promise<ReceivedResponse> {
  return options(uri, Action.noop())
}

fun HttpClient.options(uri: String, action: (RequestSpec) -> Unit): Promise<ReceivedResponse> {
  return options(URI.create(uri), Action { action(it) })
}

fun HttpClient.options(uri: String): Promise<ReceivedResponse> {
  val action: (RequestSpec) -> Unit = {}
  return options(uri, action)
}
