package ratpack.kotlin.reactor

import ratpack.exec.ExecController
import ratpack.exec.Operation
import ratpack.exec.Promise
import ratpack.func.Action
import ratpack.reactor.ReactorRatpack
import ratpack.registry.RegistrySpec
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

/**
 * Reactor extension functions.
 */

/**
 * This will initialize error hooks so that exceptions are propagated as expected.
 */
fun initialize() = ReactorRatpack.initialize()

// convert promises to fluxes
fun <T> Promise<T>.flux(): Flux<T> = ReactorRatpack.flux(this)
fun Operation.flux(): Flux<Void> = ReactorRatpack.flux(this)
fun <T, I : Iterable<T>> Promise<I>.fluxEach() = ReactorRatpack.fluxEach(this)

// convert promises to monos
fun <T> Promise<T>.mono(): Mono<T> = ReactorRatpack.mono(this)

// convert fluxes to promises
fun <T> Flux<T>.promise() = ReactorRatpack.promise(this)

// convert monos to promises
fun <T> Mono<T>.promiseSingle() = ReactorRatpack.promiseSingle(this)

// convert flowables to publishers
fun <T> Flux<T>.publisher() = ReactorRatpack.publisher(this)

// flux thread binding
fun <T> Flux<T>.bindExec() = ReactorRatpack.bindExec(this)

// flux execution forking
fun <T> Flux<T>.fork() = ReactorRatpack.fork(this)
fun <T> Flux<T>.fork(doWithRegistrySpec: Action<in RegistrySpec>) = ReactorRatpack.fork(this, doWithRegistrySpec)
fun <T> Flux<T>.fork(doWithRegistrySpec: RegistrySpec.() -> Unit) = ReactorRatpack.fork(this, doWithRegistrySpec)
fun <T> Flux<T>.forkEach() = ReactorRatpack.forkEach(this)
fun <T> Flux<T>.forkEach(doWithRegistrySpec: Action<in RegistrySpec>) = ReactorRatpack.forkEach(this, doWithRegistrySpec)
fun <T> Flux<T>.forkEach(doWithRegistrySpec: RegistrySpec.() -> Unit) = ReactorRatpack.forkEach(this, doWithRegistrySpec)

// schedulers
fun scheduler(execController: ExecController) = ReactorRatpack.computationScheduler(execController)
fun scheduler() = ReactorRatpack.computationScheduler()
fun ioScheduler(execController: ExecController) = ReactorRatpack.ioScheduler(execController)
fun ioScheduler() = ReactorRatpack.ioScheduler()



