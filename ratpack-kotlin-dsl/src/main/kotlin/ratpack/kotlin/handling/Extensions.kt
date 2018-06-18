package ratpack.kotlin.handling

import com.google.common.reflect.TypeToken
import com.google.inject.Key
import com.google.inject.Module
import com.google.inject.Provider
import com.google.inject.TypeLiteral
import com.google.inject.multibindings.Multibinder
import ratpack.exec.Promise
import ratpack.func.Action
import ratpack.func.Function
import ratpack.func.Predicate
import ratpack.guice.BindingsSpec
import ratpack.guice.ConfigurableModule
import ratpack.handling.Chain
import ratpack.handling.Context
import ratpack.handling.Handler
import ratpack.registry.Registry
import ratpack.registry.RegistrySpec
import java.util.Optional
import java.util.function.Supplier

// BindingsSpec extensions

inline fun <reified T: Module> BindingsSpec.module(): BindingsSpec = module(T::class.java)

inline fun <reified T: ConfigurableModule<C>, C> BindingsSpec.module(configurer: Action<in C>): BindingsSpec = module(T::class.java, configurer)

inline fun <reified T: ConfigurableModule<C>, C> BindingsSpec.moduleConfig(config: C, configurer: Action<in C>): BindingsSpec = moduleConfig(T::class.java, config, configurer)

inline fun <reified T: ConfigurableModule<C>, C> BindingsSpec.moduleConfig(config: C): BindingsSpec = moduleConfig(T::class.java, config)

inline fun <reified T> BindingsSpec.multiBinder(action: Action<in Multibinder<T>>): BindingsSpec = multiBinder(T::class.java, action)

inline fun <reified T> BindingsSpec.bind(): BindingsSpec = bind(T::class.java)

inline fun <reified T, reified I: T> BindingsSpec.bindType(): BindingsSpec = bind(T::class.java, I::class.java)

inline fun <reified T> BindingsSpec.multiBind(): BindingsSpec = multiBind(T::class.java)

inline fun <reified T> BindingsSpec.bindInstance(instance: T): BindingsSpec = bindInstance(T::class.java, instance)

inline fun <reified T, V: T> BindingsSpec.mulitBindInstance(instance: V): BindingsSpec = multiBindInstance(T::class.java, instance)

inline fun <reified T> BindingsSpec.provider(provider: Provider<out T>): BindingsSpec = provider(T::class.java, provider)

inline fun <reified T> BindingsSpec.multiBindProvider(provider: Provider<out T>): BindingsSpec = multiBindProvider(T::class.java, provider)

// registry

inline fun <reified T> Registry.get(): T = get(T::class.java)

inline fun <reified T> Registry.maybeGet(): Optional<T> = maybeGet(T::class.java)

inline fun <reified T> Registry.getAll(): Iterable<T> = getAll(T::class.java)

inline fun <reified T, O> Registry.first(function: Function<in T, out O>): Optional<O> = first(T::class.java, function)

inline fun <reified T> singleLazy(supplier: Supplier<out T>): Registry = Registry.singleLazy(T::class.java, supplier)

inline fun <reified T> single(implementation: T): Registry = Registry.single(T::class.java, implementation)

// Context

inline fun <reified T> Context.parse(): Promise<T> = parse(T::class.java)

inline fun <reified T, O> Context.parse(options: O): Promise<T> = parse(T::class.java, options)

// TypeToken

inline fun <reified T> typeToken() = TypeToken.of(T::class.java)

// TypeLiteral

inline fun <reified T> typeLiteral() = object : TypeLiteral<T>() {}

// Key

inline fun <reified T> key() = object : Key<T>() {}

// KChain

inline fun <reified T: Action<in Chain>> KChain.chain(): Handler = chain(T::class.java)
inline fun <reified T: Action<in Chain>> KChain.fileSystem(path: String = ""): KChain = fileSystem(path, T::class.java)
inline fun <reified T: Action<in Chain>> KChain.prefix(path: String = ""): KChain = prefix(path, T::class.java)
inline fun <reified T: Handler> KChain.all(): KChain = all(T::class.java)
inline fun <reified T: Handler> KChain.path(path: String = ""): KChain = path(path, T::class.java)
inline fun <reified T: Action<in Chain>> KChain.host(path: String = ""): KChain = host(path, T::class.java)
inline fun <reified T: Action<in Chain>> KChain.insert(): KChain = insert(T::class.java)
inline fun <reified T: Action<in Chain>> KChain.register(registry: Registry): KChain = register(registry, T::class.java)
inline fun <reified T: Action<in Chain>> KChain.register(registry: Action<in RegistrySpec>): KChain = register(registry, T::class.java)
inline fun <reified T: Handler> KChain.get(path: String = ""): KChain = get(path, T::class.java)
inline fun <reified T: Handler> KChain.put(path: String = ""): KChain = put(path, T::class.java)
inline fun <reified T: Handler> KChain.post(path: String = ""): KChain = post(path, T::class.java)
inline fun <reified T: Handler> KChain.delete(path: String = ""): KChain = delete(path, T::class.java)
inline fun <reified T: Handler> KChain.options(path: String = ""): KChain = options(path, T::class.java)
inline fun <reified T: Handler> KChain.patch(path: String = ""): KChain = patch(path, T::class.java)
inline fun <reified T: Handler> KChain.onlyIf(test: Predicate<in Context>): KChain = onlyIf(test, T::class.java)
inline fun <reified T: Action<in Chain>> KChain.`when`(test: Predicate<in Context>): KChain = `when`(test, T::class.java)
inline fun <reified T: Action<in Chain>> KChain.`when`(test: Boolean): KChain = `when`(test, T::class.java)

// KChainAction

inline fun <reified T: Action<in Chain>> KChainAction.chain(): Handler = chain(T::class.java)
inline fun <reified T: Action<in Chain>> KChainAction.fileSystem(path: String = ""): KChain = fileSystem(path, T::class.java)
inline fun <reified T: Action<in Chain>> KChainAction.prefix(path: String = ""): KChain = prefix(path, T::class.java)
inline fun <reified T: Handler> KChainAction.all(): KChain = all(T::class.java)
inline fun <reified T: Handler> KChainAction.path(path: String = ""): KChain = path(path, T::class.java)
inline fun <reified T: Action<in Chain>> KChainAction.host(path: String = ""): KChain = host(path, T::class.java)
inline fun <reified T: Action<in Chain>> KChainAction.insert(): KChain = insert(T::class.java)
inline fun <reified T: Action<in Chain>> KChainAction.register(registry: Registry): KChain = register(registry, T::class.java)
inline fun <reified T: Action<in Chain>> KChainAction.register(registry: Action<in RegistrySpec>): KChain = register(registry, T::class.java)
inline fun <reified T: Handler> KChainAction.get(path: String = ""): KChain = get(path, T::class.java)
inline fun <reified T: Handler> KChainAction.put(path: String = ""): KChain = put(path, T::class.java)
inline fun <reified T: Handler> KChainAction.post(path: String = ""): KChain = post(path, T::class.java)
inline fun <reified T: Handler> KChainAction.delete(path: String = ""): KChain = delete(path, T::class.java)
inline fun <reified T: Handler> KChainAction.options(path: String = ""): KChain = options(path, T::class.java)
inline fun <reified T: Handler> KChainAction.patch(path: String = ""): KChain = patch(path, T::class.java)
inline fun <reified T: Handler> KChainAction.onlyIf(test: Predicate<in Context>): KChain = onlyIf(test, T::class.java)
inline fun <reified T: Action<in Chain>> KChainAction.`when`(test: Predicate<in Context>): KChain = `when`(test, T::class.java)
inline fun <reified T: Action<in Chain>> KChainAction.`when`(test: Boolean): KChain = `when`(test, T::class.java)
