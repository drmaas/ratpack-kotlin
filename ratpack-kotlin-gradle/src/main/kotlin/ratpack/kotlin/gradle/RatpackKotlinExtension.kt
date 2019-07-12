package ratpack.kotlin.gradle

import org.gradle.api.Project
import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.dsl.DependencyHandler

class RatpackKotlinExtension(project: Project, val pluginVersion: String, val kotlinVersion: String) {

  companion object {
    const val GROUP = "me.drmaas"
  }

  val dependencies: DependencyHandler = project.dependencies

  fun getScript() = dependencies.create("org.jetbrains.kotlin:kotlin-script-util:${kotlinVersion}")

  fun getCompiler() = dependencies.create("org.jetbrains.kotlin:kotlin-compiler-embeddable:${kotlinVersion}")

  fun getScriptingCompiler() = dependencies.create("org.jetbrains.kotlin:kotlin-scripting-compiler-embeddable:${kotlinVersion}")

  fun getDsl() = dependency("dsl")

  fun getTest() = dependency("test")

  fun dependency(name: String) = dependencies.create("$GROUP:ratpack-kotlin-$name:$pluginVersion")

}
