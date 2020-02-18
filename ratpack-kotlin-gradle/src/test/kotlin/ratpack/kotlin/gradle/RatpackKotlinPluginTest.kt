package ratpack.kotlin.gradle

import io.kotlintest.TestCase
import io.kotlintest.TestCaseConfig
import io.kotlintest.TestResult
import io.kotlintest.shouldBe
import io.kotlintest.specs.BehaviorSpec
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome.SUCCESS
import java.io.File

class RatpackKotlinPluginTest : BehaviorSpec() {

  lateinit var testProjectDir: File

  override val defaultTestCaseConfig = TestCaseConfig(enabled = false)

  override fun beforeTest(testCase: TestCase) {
    testProjectDir = File.createTempFile("ratpack-kotlin", ".tmp")
    testProjectDir.deleteOnExit()
  }

  override fun afterTest(testCase: TestCase, result: TestResult) {
    testProjectDir.delete()
  }

  init {
    given("build script") {
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
          .withProjectDir(testProjectDir)
          .withArguments("shadowJar")
          .build()

        then("it worked ok") {
          result.output.contains("Successfully resolved URL 'http://www.google.com/'")
          result.task(":shadowJar")?.outcome shouldBe SUCCESS
          testProjectDir.delete()
        }
      }
    }
  }

  // build the file with the supplied text
  private fun withBuildFile(definition: () -> String) {
    val buildFile = File(testProjectDir, "build.gradle.kts")
    buildFile.printWriter().use {
      it.println(definition.invoke())
    }
  }

}


