package ratpack.kotlin.gradle

import org.gradle.api.Project
import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.dsl.DependencyHandler

class RatpackKotlinExtension(project: Project, val pluginVersion: String) {

  companion object {
    const val GROUP = "me.drmaas"
  }

  val dependencies: DependencyHandler = project.dependencies

  fun getDsl(): Dependency {
    return dependency("dsl")
  }

  fun getTest(): Dependency {
    return dependency("test")
  }

  fun dependency(name: String): Dependency {
    return dependencies.create("$GROUP:ratpack-kotlin-$name:$pluginVersion")
  }

}
