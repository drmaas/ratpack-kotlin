package ratpack.kotlin.client

import ratpack.exec.Promise
import ratpack.func.Action
import ratpack.http.client.HttpClient
import ratpack.http.client.ReceivedResponse
import ratpack.http.client.RequestSpec
import java.net.URI

class KHttpClient(val client: HttpClient) : HttpClient by client {

  fun get(uri: String, action: (RequestSpec) -> Unit): Promise<ReceivedResponse> {
    return get(URI.create(uri), action)
  }

  fun get(uri: String): Promise<ReceivedResponse> {
    val action: (RequestSpec) -> Unit = {}
    return get(uri, action)
  }

  fun put(uri: URI, action: Action<in RequestSpec>): Promise<ReceivedResponse> {
    return request(uri, action.prepend { it.put() })
  }

  fun put(uri: URI): Promise<ReceivedResponse> {
    return put(uri, Action.noop())
  }

  fun put(uri: String, action: (RequestSpec) -> Unit): Promise<ReceivedResponse> {
    return put(URI.create(uri), Action { action(it) })
  }

  fun put(uri: String): Promise<ReceivedResponse> {
    val action: (RequestSpec) -> Unit = {}
    return put(uri, action)
  }

  fun post(uri: String, action: (RequestSpec) -> Unit): Promise<ReceivedResponse> {
    return post(URI.create(uri), action)
  }

  fun post(uri: String): Promise<ReceivedResponse> {
    val action: (RequestSpec) -> Unit = {}
    return post(uri, action)
  }

  fun delete(uri: URI, action: Action<in RequestSpec>): Promise<ReceivedResponse> {
    return client.request(uri, action.prepend { it.delete() })
  }

  fun delete(uri: URI): Promise<ReceivedResponse> {
    return delete(uri, Action.noop())
  }

  fun delete(uri: String, action: (RequestSpec) -> Unit): Promise<ReceivedResponse> {
    return delete(URI.create(uri), Action { action(it) })
  }

  fun delete(uri: String): Promise<ReceivedResponse> {
    val action: (RequestSpec) -> Unit = {}
    return delete(uri, action)
  }

  fun patch(uri: URI, action: Action<in RequestSpec>): Promise<ReceivedResponse> {
    return request(uri, action.prepend { it.patch() })
  }

  fun patch(uri: URI): Promise<ReceivedResponse> {
    return patch(uri, Action.noop())
  }

  fun patch(uri: String, action: (RequestSpec) -> Unit): Promise<ReceivedResponse> {
    return patch(URI.create(uri), Action { action(it) })
  }

  fun patch(uri: String): Promise<ReceivedResponse> {
    val action: (RequestSpec) -> Unit = {}
    return patch(uri, action)
  }

  fun options(uri: URI, action: Action<in RequestSpec>): Promise<ReceivedResponse> {
    return request(uri, action.prepend { it.options() })
  }

  fun options(uri: URI): Promise<ReceivedResponse> {
    return options(uri, Action.noop())
  }

  fun options(uri: String, action: (RequestSpec) -> Unit): Promise<ReceivedResponse> {
    return options(URI.create(uri), Action { action(it) })
  }

  fun options(uri: String): Promise<ReceivedResponse> {
    val action: (RequestSpec) -> Unit = {}
    return options(uri, action)
  }

  fun head(uri: URI, action: Action<in RequestSpec>): Promise<ReceivedResponse> {
    return request(uri, action.prepend { it.head() })
  }

  fun head(uri: URI): Promise<ReceivedResponse> {
    return head(uri, Action.noop())
  }

  fun head(uri: String, action: (RequestSpec) -> Unit): Promise<ReceivedResponse> {
    return head(URI.create(uri), Action { action(it) })
  }

  fun head(uri: String): Promise<ReceivedResponse> {
    val action: (RequestSpec) -> Unit = {}
    return head(uri, action)
  }

}
