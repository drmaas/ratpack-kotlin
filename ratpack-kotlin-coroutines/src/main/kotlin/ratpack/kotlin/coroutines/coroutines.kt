/**
 *  All credit goes to https://github.com/gregopet for inspiration!
 */

package ratpack.kotlin.coroutines

import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers.Unconfined
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import ratpack.exec.Blocking
import ratpack.exec.Promise
import ratpack.handling.Context
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 *
 * Runs the given block on the request thread.
 *
 * Frees the request thread as soon as the first <tt>await<tt> (or any other suspendable function) is encountered and
 * continues work on another appropriate thread after the operation finishes.
 *
 * @param block The block to execute
 */
@kotlinx.coroutines.ExperimentalCoroutinesApi
fun Context.async(block: suspend () -> Any?) {
  GlobalScope.launch(Unconfined, CoroutineStart.UNDISPATCHED) {
    try {
      block()
    } catch (t: Throwable) {
      this@async.error(t)
    }
  }
}

/**
 * Returns the result of the [block] as soon as the blocking computation completes. The request thread is released
 * during the blocking operation.
 */
suspend fun <T> await(block: () -> T): T = Blocking.get(block).await()

/**
 * Resolves the promise and returns its value as soon as the blocking computation completes. The request thread is released
 * during the blocking operation.
 */
suspend fun <T> Promise<T>.await(): T = suspendCancellableCoroutine { cont: CancellableContinuation<T> ->
  this.onError { cont.resumeWithException(it) }.then { cont.resume(it) }
}
