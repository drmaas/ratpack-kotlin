package ratpack.kotlin.gradle

import netflix.nebula.NebulaKotlinPlugin
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.ApplicationPluginConvention
import org.gradle.util.GradleVersion
import ratpack.gradle.RatpackPlugin

class RatpackKotlinPlugin : Plugin<Project> {

  private val GRADLE_VERSION_BASELINE = GradleVersion.version("4.0")

  override fun apply(project: Project) {
    with(project) {
      val gradleVersion = GradleVersion.version(gradle.gradleVersion)
      if (gradleVersion < GRADLE_VERSION_BASELINE) {
        throw GradleException("Ratpack requires Gradle version ${GRADLE_VERSION_BASELINE.version} or later")
      }

      plugins.apply(RatpackPlugin::class.java)
      plugins.apply(NebulaKotlinPlugin::class.java)

      val application = convention.findPlugin(ApplicationPluginConvention::class.java)
      application?.mainClassName = "ratpack.kotlin.runner.KotlinDslRunner"

      val ratpackKotlinExtension = RatpackKotlinExtension(project, project.version.toString()) // this is just used to add dependencies

      dependencies.add("compile", ratpackKotlinExtension.getDsl())
      dependencies.add("testCompile", ratpackKotlinExtension.getTest())
    }

  }

}
