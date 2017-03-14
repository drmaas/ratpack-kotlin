package ratpack.kotlin.rx

import ratpack.exec.ExecController
import ratpack.exec.Operation
import ratpack.exec.Promise
import ratpack.func.Action
import ratpack.registry.RegistrySpec
import ratpack.rx.RxRatpack
import rx.Observable

/**
 * rx extension functions.
 * see https://ratpack.io/manual/current/api/ratpack/rx/RxRatpack.html for details.
 */

/**
 * @see RxRatpack.initialize
 */
fun initialize() = RxRatpack.initialize()

// convert promises to observables
fun <T> Promise<T>.observe(): Observable<T> = RxRatpack.observe(this)
fun Operation.observe(): Observable<Void> = RxRatpack.observe(this)
fun <T, I : Iterable<T>> Promise<I>.observeEach() = RxRatpack.observeEach(this)

// convert observables to promises
fun <T> Observable<T>.promise() = RxRatpack.promise(this)
fun <T> Observable.OnSubscribe<T>.promise() = RxRatpack.promise(this)
fun <T> Observable.OnSubscribe<T>.promiseSingle() = RxRatpack.promiseSingle(this)

// convert observables to publishers
fun <T> Observable<T>.publisher() = RxRatpack.publisher(this)
fun <T> Observable.OnSubscribe<T>.publisher() = RxRatpack.publisher(this)

// observable thread binding
fun <T> Observable<T>.bindExec() = RxRatpack.bindExec(this)
fun <T> Observable<T>.fork() = RxRatpack.fork(this)
fun <T> Observable<T>.fork(doWithRegistrySpec: Action<in RegistrySpec>) = RxRatpack.fork(this, doWithRegistrySpec)
fun <T> Observable<T>.fork(doWithRegistrySpec: RegistrySpec.() -> Unit) = RxRatpack.fork(this, doWithRegistrySpec)
fun <T> Observable<T>.forkEach() = RxRatpack.forkEach(this)
fun <T> Observable<T>.forkEach(doWithRegistrySpec: Action<in RegistrySpec>) = RxRatpack.forkEach(this, doWithRegistrySpec)
fun <T> Observable<T>.forkEach(doWithRegistrySpec: RegistrySpec.() -> Unit) = RxRatpack.forkEach(this, doWithRegistrySpec)

// schedulers
fun scheduler(execController: ExecController) = RxRatpack.scheduler(execController)
fun scheduler() = RxRatpack.scheduler()



