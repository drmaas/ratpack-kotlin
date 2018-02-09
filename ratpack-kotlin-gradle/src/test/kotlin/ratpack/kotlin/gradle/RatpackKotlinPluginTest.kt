package ratpack.kotlin.gradle

import io.kotlintest.matchers.shouldEqual
import io.kotlintest.specs.BehaviorSpec
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome.SUCCESS
import org.junit.rules.TemporaryFolder

class RatpackKotlinPluginTest : BehaviorSpec() {
  val testProjectDir = TemporaryFolder()
  init {
    given("build script") {
      testProjectDir.create()
      withBuildFile {
        """
buildscript {
  repositories {
    mavenLocal()
    jcenter()
  }
  dependencies {
    classpath("me.drmaas:ratpack-kotlin-gradle:1.2.0-dev.0.uncommitted+764c238")
  }
}

repositories {
  mavenLocal()
  jcenter()
}

plugins.apply("me.drmaas.ratpack-kotlin")

group = "com.example"
version = "0.0.1"
        """
      }

      `when`("build file is executed") {
        val result = GradleRunner.create()
          .withProjectDir(testProjectDir.root)
          .withArguments("shadowJar")
          .build()

        then("it worked ok") {
          result.output.contains("Successfully resolved URL 'http://www.google.com/'")
          result.task(":shadowJar")?.outcome shouldEqual SUCCESS
          testProjectDir.delete()
        }
      }
    }
  }

  // build the file with the supplied text
  private fun withBuildFile(definition: () -> String) {
    val buildFile = testProjectDir.newFile("build.gradle.kts")
    buildFile.printWriter().use {
      it.println(definition.invoke())
    }
  }

}


