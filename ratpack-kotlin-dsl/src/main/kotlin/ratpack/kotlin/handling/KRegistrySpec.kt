package ratpack.kotlin.handling

import ratpack.registry.RegistrySpec

class KRegistrySpec(val delegate: RegistrySpec) : RegistrySpec by delegate