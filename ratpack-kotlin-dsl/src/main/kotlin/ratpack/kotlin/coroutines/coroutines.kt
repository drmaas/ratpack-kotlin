/**
 *  All credit goes to https://github.com/gregopet for inspiration!
 */

package ratpack.kotlin.coroutines

import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers.Unconfined
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import ratpack.exec.Blocking
import ratpack.exec.Downstream
import ratpack.exec.ExecSpec
import ratpack.exec.Promise
import ratpack.handling.Context
import ratpack.kotlin.handling.KContext
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
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
@OptIn(DelicateCoroutinesApi::class)
@ExperimentalCoroutinesApi
fun Context.async(block: suspend CoroutineScope.() -> Any?) {
  GlobalScope.launch(Unconfined, CoroutineStart.UNDISPATCHED) {
    try {
      block()
    } catch (t: Throwable) {
      this@async.error(t)
    }
  }
}

@ExperimentalCoroutinesApi
fun KContext.async(cb: suspend CoroutineScope.() -> Any?) = this.context.async(cb)

/**
 * Returns the result of the [block] as soon as the blocking computation completes. The request thread is released
 * during the blocking operation.
 */
suspend fun <T> await(block: () -> T): T = Blocking.get(block).await()

/**
 * Returns the result of the [block] as soon as the blocking computation completes. The request thread is released
 * during the blocking operation.
 */
fun <T> defer(block: () -> T): Deferred<T> = Blocking.get(block).defer()

/**
 * Resolves the promise and returns its value as soon as the blocking computation completes. The request thread is released
 * during the blocking operation.
 */
suspend fun <T> Promise<T>.await(fork: Boolean = false, onStart: (ExecSpec) -> Unit = {}): T = suspendCancellableCoroutine { cont: CancellableContinuation<T> ->
  if (fork) { this.fork(onStart) } else { this }
    .onError {
      cont.resumeWithException(it)
    }.then {
      cont.resume(it)
    }
}

fun <T> lazyDefer(mode: LazyThreadSafetyMode = LazyThreadSafetyMode.NONE, deferred: Deferred<T>): Lazy<Deferred<T>> = lazy(mode) { deferred }

fun <T> lazyDeferIf(predicate: () -> Boolean, deferred: Deferred<T>): Lazy<Deferred<T>?> = lazy(LazyThreadSafetyMode.NONE) { if (predicate()) deferred else null }

/**
 * Convert this [Promise] into a [Deferred] by launching an async coroutine, inside of which
 * the promised value will be resolved.
 */
@OptIn(DelicateCoroutinesApi::class)
fun <T> Promise<T>.defer(fork: Boolean = false, onStart: (ExecSpec) -> Unit = {}): Deferred<T> {
  return GlobalScope.async(Unconfined, CoroutineStart.UNDISPATCHED) {
    this@defer.await(fork, onStart)
  }
}

/**
 * Run a block of code asynchronously in a new coroutine, but do not start it until it is referenced.
 */
fun <T> lazyAsync(mode: LazyThreadSafetyMode = LazyThreadSafetyMode.NONE, block: suspend CoroutineScope.() -> T): Lazy<Deferred<T>> = lazy(mode) { async(block = block) }

fun <T> lazyAsyncIf(predicate: () -> Boolean, block: suspend CoroutineScope.() -> T): Lazy<Deferred<T>?> = lazy(LazyThreadSafetyMode.NONE) { if (predicate()) async(block = block) else null }

/**
 * Convert this block into a [Deferred] by launching an async coroutine, inside of which
 * the block will be resolved.
 */
@OptIn(DelicateCoroutinesApi::class)
fun <T> async(start: CoroutineStart = CoroutineStart.UNDISPATCHED, block: suspend CoroutineScope.() -> T): Deferred<T> {
  return GlobalScope.async(Unconfined, start, block = block)
}

/**
 * Consume the promises in parallel and execute the zipper function on the results.
 */
suspend fun <T1, T2, R> zip(p1: Promise<T1>, p2: Promise<T2>, onStart: (ExecSpec) -> Unit = {}, zipper: (T1, T2) -> R): R {
  val d1 = p1.defer(true, onStart)
  val d2 = p2.defer(true, onStart)
  return zipper(d1.await(), d2.await())
}

/**
 * Consume the promises in parallel and execute the zipper function on the results.
 */
suspend fun <T1, T2, T3, R> zip(p1: Promise<T1>, p2: Promise<T2>, p3: Promise<T3>,
                                onStart: (ExecSpec) -> Unit = {}, zipper: (T1, T2, T3) -> R): R {
  val d1 = p1.defer(true, onStart)
  val d2 = p2.defer(true, onStart)
  val d3 = p3.defer(true, onStart)
  return zipper(d1.await(), d2.await(), d3.await())
}

/**
 * Consume the promises in parallel and execute the zipper function on the results.
 */
suspend fun <T1, T2, T3, T4, R> zip(p1: Promise<T1>, p2: Promise<T2>, p3: Promise<T3>,
                                    p4: Promise<T4>, onStart: (ExecSpec) -> Unit = {}, zipper: (T1, T2, T3, T4) -> R): R {
  val d1 = p1.defer(true, onStart)
  val d2 = p2.defer(true, onStart)
  val d3 = p3.defer(true, onStart)
  val d4 = p4.defer(true, onStart)
  return zipper(d1.await(), d2.await(), d3.await(), d4.await())
}

/**
 * Consume the promises in parallel and execute the zipper function on the results.
 */
suspend fun <T1, T2, T3, T4, T5, R> zip(p1: Promise<T1>, p2: Promise<T2>, p3: Promise<T3>,
                                        p4: Promise<T4>, p5: Promise<T5>, onStart: (ExecSpec) -> Unit = {},
                                        zipper: (T1, T2, T3, T4, T5) -> R): R {
  val d1 = p1.defer(true, onStart)
  val d2 = p2.defer(true, onStart)
  val d3 = p3.defer(true, onStart)
  val d4 = p4.defer(true, onStart)
  val d5 = p5.defer(true, onStart)
  return zipper(d1.await(), d2.await(), d3.await(), d4.await(), d5.await())
}

/**
 * Consume the promises in parallel and execute the zipper function on the results.
 */
suspend fun <T1, T2, T3, T4, T5, T6, R> zip(p1: Promise<T1>, p2: Promise<T2>, p3: Promise<T3>,
                                            p4: Promise<T4>, p5: Promise<T5>, p6: Promise<T6>,
                                            onStart: (ExecSpec) -> Unit = {}, zipper: (T1, T2, T3, T4, T5, T6) -> R): R {
  val d1 = p1.defer(true, onStart)
  val d2 = p2.defer(true, onStart)
  val d3 = p3.defer(true, onStart)
  val d4 = p4.defer(true, onStart)
  val d5 = p5.defer(true, onStart)
  val d6 = p6.defer(true, onStart)
  return zipper(d1.await(), d2.await(), d3.await(), d4.await(), d5.await(), d6.await())
}

/**
 * Consume the promises in parallel and execute the zipper function on the results.
 */
suspend fun <T1, T2, T3, T4, T5, T6, T7, R> zip(p1: Promise<T1>, p2: Promise<T2>, p3: Promise<T3>,
                                                p4: Promise<T4>, p5: Promise<T5>, p6: Promise<T6>,
                                                p7: Promise<T7>, onStart: (ExecSpec) -> Unit = {},
                                                zipper: (T1, T2, T3, T4, T5, T6, T7) -> R): R {
  val d1 = p1.defer(true, onStart)
  val d2 = p2.defer(true, onStart)
  val d3 = p3.defer(true, onStart)
  val d4 = p4.defer(true, onStart)
  val d5 = p5.defer(true, onStart)
  val d6 = p6.defer(true, onStart)
  val d7 = p7.defer(true, onStart)
  return zipper(d1.await(), d2.await(), d3.await(), d4.await(), d5.await(), d6.await(), d7.await())
}

/**
 * Consume the promises in parallel and execute the zipper function on the results.
 */
suspend fun <T1, T2, T3, T4, T5, T6, T7, T8, R> zip(p1: Promise<T1>, p2: Promise<T2>, p3: Promise<T3>,
                                                    p4: Promise<T4>, p5: Promise<T5>, p6: Promise<T6>,
                                                    p7: Promise<T7>, p8: Promise<T8>, onStart: (ExecSpec) -> Unit = {},
                                                    zipper: (T1, T2, T3, T4, T5, T6, T7, T8) -> R): R {
  val d1 = p1.defer(true, onStart)
  val d2 = p2.defer(true, onStart)
  val d3 = p3.defer(true, onStart)
  val d4 = p4.defer(true, onStart)
  val d5 = p5.defer(true, onStart)
  val d6 = p6.defer(true, onStart)
  val d7 = p7.defer(true, onStart)
  val d8 = p8.defer(true, onStart)
  return zipper(d1.await(), d2.await(), d3.await(), d4.await(), d5.await(), d6.await(), d7.await(), d8.await())
}

/**
 * Consume the promises in parallel and execute the zipper function on the results.
 */
suspend fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, R> zip(p1: Promise<T1>, p2: Promise<T2>, p3: Promise<T3>,
                                                        p4: Promise<T4>, p5: Promise<T5>, p6: Promise<T6>,
                                                        p7: Promise<T7>, p8: Promise<T8>, p9: Promise<T9>,
                                                        onStart: (ExecSpec) -> Unit = {}, zipper: (T1, T2, T3, T4, T5, T6, T7, T8, T9) -> R): R {
  val d1 = p1.defer(true, onStart)
  val d2 = p2.defer(true, onStart)
  val d3 = p3.defer(true, onStart)
  val d4 = p4.defer(true, onStart)
  val d5 = p5.defer(true, onStart)
  val d6 = p6.defer(true, onStart)
  val d7 = p7.defer(true, onStart)
  val d8 = p8.defer(true, onStart)
  val d9 = p9.defer(true, onStart)
  return zipper(d1.await(), d2.await(), d3.await(), d4.await(), d5.await(), d6.await(), d7.await(), d8.await(), d9.await())
}

/**
 * Creates a [promise][Promise] that will run a given [block] in a coroutine.
 */
@OptIn(DelicateCoroutinesApi::class)
@ExperimentalCoroutinesApi
fun <T> promise(
  scope: CoroutineScope = GlobalScope,
  context: CoroutineContext = EmptyCoroutineContext,
  block: suspend CoroutineScope.() -> T
): Promise<T> = Promise.async { downstream ->
  handlePromiseInternal(scope, context, downstream, block)
}

private fun <T> handlePromiseInternal(
  scope: CoroutineScope,
  context: CoroutineContext = EmptyCoroutineContext,
  downstream: Downstream<T>,
  block: suspend CoroutineScope.() -> T) {
  scope.launch(context, CoroutineStart.UNDISPATCHED) {
    try {
      downstream.success(block())
    } catch (e: Throwable) {
      downstream.error(e)
    }
  }

}
