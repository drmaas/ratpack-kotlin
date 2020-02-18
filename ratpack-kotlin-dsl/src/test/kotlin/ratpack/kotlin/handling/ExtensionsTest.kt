package ratpack.kotlin.handling

import com.google.inject.AbstractModule
import com.google.inject.Provider
import io.kotlintest.shouldBe
import io.kotlintest.specs.BehaviorSpec
import ratpack.func.Action
import ratpack.guice.BindingsSpec
import ratpack.guice.ConfigurableModule
import ratpack.guice.Guice
import ratpack.registry.Registry
import ratpack.server.ServerConfig

class ExtensionsTest : BehaviorSpec() {
  init {
    given("Module test") {
      val registry = initialRegistry()
      `when`("module 1") {
        val result = registry(registry) {
          it.module<MyModule>()
        }
        then("it works") {
          result.get<String>() shouldBe "test"
        }
      }
      `when`("module 2") {
        val result = registry(registry) {
          it.module<MyConfigurableModule, MyConfig>( Action {
            it.x = "x"
          })
        }
        then("it works") {
          result.get<String>() shouldBe "test"
          result.get<MyConfig>().x shouldBe "x"
        }
      }
      `when`("moduleConfig 1") {
        val result = registry(registry) {
          it.moduleConfig<MyConfigurableModule, MyConfig>(MyConfig("x"), Action {
            it.x = "y"
          })
        }
        then("it works") {
          result.get<String>() shouldBe "test"
          result.get<MyConfig>().x shouldBe "y"
        }
      }
      `when`("moduleConfig 2") {
        val result = registry(registry) {
          it.moduleConfig<MyConfigurableModule, MyConfig>(MyConfig("x"))
        }
        then("it works") {
          result.get<String>() shouldBe "test"
          result.get<MyConfig>().x shouldBe "x"
        }
      }
      `when`("multiBinder") {
        val result = registry(registry) {
          it.multiBinder<String>(Action {
            it.addBinding().toInstance("x")
            it.addBinding().toInstance("y")
          })
        }
        then("it works") {
          result.get<Set<String>>() shouldBe setOf("x","y")
        }
      }
      `when`("bind") {
        val result = registry(registry) {
          it.bind<String>()
        }
        then("it works") {
          result.get<String>() shouldBe ""
        }
      }
      `when`("bindType") {
        val result = registry(registry) {
          it.bindType<Test, Test2>()
        }
        then("it works") {
          result.get<Test>().x shouldBe ""
        }
      }
      `when`("multiBind") {
        val result = registry(registry) {
          it.multiBind<Test>()
        }
        then("it works") {
          result.get<Test>().x shouldBe ""
        }
      }
      `when`("bindInstance") {
        val result = registry(registry) {
          it.bindInstance<Test>(Test("1"))
        }
        then("it works") {
          result.get<Test>().x shouldBe "1"
        }
      }
      `when`("multiBindInstance") {
        val result = registry(registry) {
          it.multiBindInstance<Test3>(Test3("1")).multiBindInstance<Test3>(Test3("2"))
        }
        then("it works") {
          result.get<Set<Test>>() shouldBe setOf(Test3("1"), Test3("2"))
        }
      }
      `when`("provider") {
        val result = registry(registry) {
          it.provider(Test3Provider())
        }
        then("it works") {
          result.get<Test3>() shouldBe Test3("1")
        }
      }
      `when`("multiBindProvider") {
        val result = registry(registry) {
          it.multiBindProvider(Test3Provider2("1")).multiBindProvider(Test3Provider2("2"))
        }
        then("it works") {
          result.get<Set<Test>>() shouldBe setOf(Test3("1"), Test3("2"))
        }
      }
    }
  }
}

fun initialRegistry(): Registry {
  return Registry.of {
    it.add(ServerConfig.builder().build())
  }
}

fun registry(registry: Registry, spec: (BindingsSpec) -> Unit): Registry {
  return Guice.registry(spec).apply(registry)
}

class MyModule: AbstractModule() {
  override fun configure() {
    bind(String::class.java).toInstance("test")
  }
}

data class MyConfig(var x: String? = null)

class MyConfigurableModule: ConfigurableModule<MyConfig>() {
  override fun configure() {
    bind(String::class.java).toInstance("test")
  }
}

open class Test(var x: String? = "")

class Test2: Test()

data class Test3(var x: String? = "")

class Test3Provider: Provider<Test3> {
  override fun get(): Test3 {
    return Test3("1")
  }
}

class Test3Provider2(val x: String): Provider<Test3> {
  override fun get(): Test3 {
    return Test3(x)
  }
}
