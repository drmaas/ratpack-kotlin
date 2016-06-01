package ratpack.kotlin.handling

import ratpack.file.FileHandlerSpec
import ratpack.handling.Chain
import ratpack.handling.Handler
import ratpack.registry.Registry
import ratpack.registry.RegistrySpec

class KChain (val delegate: Chain) : Chain by delegate {
    fun chain(cb: KChain.() -> Unit) : Handler = delegate.chain { KChain(it).(cb)() }
    fun files(cb: FileHandlerSpec.() -> Unit) : KChain = KChain(delegate.files { it.(cb)() })
    fun fileSystem(path: String = "", cb: KChain.() -> Unit) : KChain = KChain(delegate.fileSystem (path) { KChain(it).(cb)() })
    fun prefix(path: String = "", cb: KChain.() -> Unit) : KChain = KChain(delegate.prefix (path) { KChain(it).(cb)() })
    fun all(cb: KContext.() -> Unit) : KChain = KChain(delegate.all { KContext(it).(cb)() })
    fun path(path: String = "", cb: KContext.() -> Unit) : KChain = KChain(delegate.path (path) { KContext(it).(cb)() })
    fun host(hostName: String, cb: KChain.() -> Unit) : KChain = KChain(delegate.host (hostName) { KChain(it).(cb)() })
    fun insert(cb: KChain.() -> Unit) : KChain = KChain(delegate.insert () { KChain(it).(cb)() })
    fun register(registryAction: RegistrySpec.() -> Unit, cb: KChain.() -> Unit) : KChain = KChain(delegate.register({ KRegistrySpec(it).(registryAction)() }, { KChain(it).(cb)() }))
    fun register(registry: Registry, cb: KChain.() -> Unit) : KChain = KChain(delegate.register(registry) { KChain(it).(cb)() })
    fun register(registryAction: RegistrySpec.() -> Unit) : KChain = KChain(delegate.register { KRegistrySpec(it).(registryAction)() })
    @Suppress("ReplaceGetOrSet")
    fun get(path: String = "", cb: KContext.() -> Unit) : KChain = KChain(delegate.get (path) { KContext(it).(cb)() })
    fun put(path: String = "", cb: KContext.() -> Unit) : KChain = KChain(delegate.put (path) { KContext(it).(cb)() })
    fun post(path: String = "", cb: KContext.() -> Unit) : KChain = KChain(delegate.post (path) { KContext(it).(cb)() })
    fun delete(path: String = "", cb: KContext.() -> Unit) : KChain = KChain(delegate.delete (path) { KContext(it).(cb)() })
    fun options(path: String = "", cb: KContext.() -> Unit) : KChain = KChain(delegate.options (path) { KContext(it).(cb)() })
    fun patch(path: String = "", cb: KContext.() -> Unit) : KChain = KChain(delegate.patch (path) { KContext(it).(cb)() })
}
