package ratpack.kotlin.rx2

import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.FlowableOnSubscribe
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Single
import ratpack.exec.ExecController
import ratpack.exec.Operation
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
fun <T> Promise<T>.observe(): Observable<T> = RxRatpack.observe(this)

fun Operation.observe(): Observable<Void> = RxRatpack.observe(this)
fun <T, I : Iterable<T>> Promise<I>.observeEach() = RxRatpack.observeEach(this)

// convert observables to promises
fun <T> Observable<T>.promise() = RxRatpack.promise(this)

fun <T> ObservableOnSubscribe<T>.promise() = RxRatpack.promise(this)
fun <T> Observable<T>.promiseSingle() = RxRatpack.promiseSingle(this)
fun <T> ObservableOnSubscribe<T>.promiseSingle() = RxRatpack.promiseSingle(this)
fun <T> Single<T>.promiseSingle() = RxRatpack.promiseSingle(this)

// convert observables to publishers
fun <T> Observable<T>.publisher(strategy: BackpressureStrategy) = RxRatpack.publisher(this, strategy)

fun <T> ObservableOnSubscribe<T>.publisher(strategy: BackpressureStrategy) = RxRatpack.publisher(this, strategy)

// observable thread binding
fun <T> Observable<T>.bindExec() = RxRatpack.bindExec(this)

fun <T> Observable<T>.fork() = RxRatpack.fork(this)
fun <T> Observable<T>.fork(doWithRegistrySpec: Action<in RegistrySpec>) = RxRatpack.fork(this, doWithRegistrySpec)
fun <T> Observable<T>.fork(doWithRegistrySpec: RegistrySpec.() -> Unit) = RxRatpack.fork(this, doWithRegistrySpec)
fun <T> Observable<T>.forkEach() = RxRatpack.forkEach(this)
fun <T> Observable<T>.forkEach(doWithRegistrySpec: Action<in RegistrySpec>) = RxRatpack.forkEach(this, doWithRegistrySpec)
fun <T> Observable<T>.forkEach(doWithRegistrySpec: RegistrySpec.() -> Unit) = RxRatpack.forkEach(this, doWithRegistrySpec)

// convert promises to flowables
fun <T> Promise<T>.flow(strategy: BackpressureStrategy): Flowable<T> = RxRatpack.flow(this, strategy)

fun Operation.flow(strategy: BackpressureStrategy): Flowable<Void> = RxRatpack.flow(this, strategy)
fun <T, I : Iterable<T>> Promise<I>.flowEach(strategy: BackpressureStrategy) = RxRatpack.flowEach(this, strategy)

// convert flowables to promises
fun <T> Flowable<T>.promise() = RxRatpack.promise(this)

fun <T> FlowableOnSubscribe<T>.promise(strategy: BackpressureStrategy) = RxRatpack.promise(this, strategy)
fun <T> Flowable<T>.promiseSingle() = RxRatpack.promiseSingle(this)
fun <T> FlowableOnSubscribe<T>.promiseSingle(strategy: BackpressureStrategy) = RxRatpack.promiseSingle(this, strategy)

// convert flowables to publishers
fun <T> Flowable<T>.publisher() = RxRatpack.publisher(this)

fun <T> FlowableOnSubscribe<T>.publisher(strategy: BackpressureStrategy) = RxRatpack.publisher(this, strategy)

// flowable thread binding
fun <T> Flowable<T>.bindExec(strategy: BackpressureStrategy) = RxRatpack.bindExec(this, strategy)

fun <T> Flowable<T>.fork(strategy: BackpressureStrategy) = RxRatpack.fork(this, strategy)
fun <T> Flowable<T>.fork(doWithRegistrySpec: Action<in RegistrySpec>, strategy: BackpressureStrategy) = RxRatpack.fork(this, strategy, doWithRegistrySpec)
fun <T> Flowable<T>.fork(doWithRegistrySpec: RegistrySpec.() -> Unit, strategy: BackpressureStrategy) = RxRatpack.fork(this, strategy, doWithRegistrySpec)
fun <T> Flowable<T>.forkEach() = RxRatpack.forkEach(this)
fun <T> Flowable<T>.forkEach(doWithRegistrySpec: Action<in RegistrySpec>) = RxRatpack.forkEach(this, doWithRegistrySpec)
fun <T> Flowable<T>.forkEach(doWithRegistrySpec: RegistrySpec.() -> Unit) = RxRatpack.forkEach(this, doWithRegistrySpec)

// schedulers
fun scheduler(execController: ExecController) = RxRatpack.scheduler(execController)

fun scheduler() = RxRatpack.scheduler()



