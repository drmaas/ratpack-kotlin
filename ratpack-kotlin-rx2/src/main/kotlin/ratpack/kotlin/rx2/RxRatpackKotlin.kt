package ratpack.kotlin.rx2

import io.reactivex.BackpressureStrategy
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Single
import io.reactivex.SingleOnSubscribe
import ratpack.exec.ExecController
import ratpack.exec.Promise
import ratpack.func.Action
import ratpack.registry.RegistrySpec
import ratpack.rx2.RxRatpack

/**
 * RxJava2 extension functions.
 * see https://ratpack.io/manual/current/api/ratpack/rx/RxRatpack.html for details.
 */

/**
 * @see RxRatpack.initialize
 */
fun initialize() = RxRatpack.initialize()

// convert promises to observables
fun <T, I : Iterable<T>> Promise<I>.observe() = RxRatpack.observe(this)

// convert singles to promises
fun <T> Single<T>.promise() = RxRatpack.promise(this)
fun <T> SingleOnSubscribe<T>.promise() = RxRatpack.promise(this)

// convert observables to promises
fun <T> Observable<T>.promiseAll() = RxRatpack.promiseAll(this)
fun <T> ObservableOnSubscribe<T>.promiseAll() = RxRatpack.promiseAll(this)

// convert observables to publishers
fun <T> Observable<T>.publisher(strategy: BackpressureStrategy) = RxRatpack.publisher(this, strategy)

fun <T> ObservableOnSubscribe<T>.publisher(strategy: BackpressureStrategy) = RxRatpack.publisher(this, strategy)

// observable thread binding
fun <T> Observable<T>.bindExec() = RxRatpack.bindExec(this)

// forking
fun <T> Observable<T>.fork() = RxRatpack.fork(this)
fun <T> Observable<T>.fork(doWithRegistrySpec: Action<in RegistrySpec>) = RxRatpack.fork(this, doWithRegistrySpec)
fun <T> Observable<T>.fork(doWithRegistrySpec: RegistrySpec.() -> Unit) = RxRatpack.fork(this, doWithRegistrySpec)
fun <T> Observable<T>.forkEach() = RxRatpack.forkEach(this)
fun <T> Observable<T>.forkEach(doWithRegistrySpec: Action<in RegistrySpec>) = RxRatpack.forkEach(this, doWithRegistrySpec)
fun <T> Observable<T>.forkEach(doWithRegistrySpec: RegistrySpec.() -> Unit) = RxRatpack.forkEach(this, doWithRegistrySpec)

// convert promise to single
fun <T> Promise<T>.single(): Single<T> = RxRatpack.single(this)

// schedulers
fun scheduler(execController: ExecController) = RxRatpack.scheduler(execController)

fun scheduler() = RxRatpack.scheduler()



