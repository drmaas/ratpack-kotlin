// First, apply the publishing plugin
plugins {
  id("com.gradle.plugin-publish") version "0.10.0"
  `java-gradle-plugin`
}

repositories {
  jcenter()
}

apply {
  from ("../dependencies.gradle")
}

dependencies {
  // other dependencies that your plugin requires
  compile("io.ratpack:ratpack-gradle:${(project.ext["commonVersions"] as Map<String, String>)["ratpack"]}")
  compile("com.netflix.nebula:nebula-kotlin-plugin:${(project.ext["commonVersions"] as Map<String, String>)["kotlin"]}")

  testCompile("io.kotlintest:kotlintest:${(project.ext["commonVersions"] as Map<String, String>)["kotlinTest"]}")
}

gradlePlugin {
  plugins {
    create("ratpackKotlinPlugin") {
      id = "me.drmaas.ratpack-kotlin"
      implementationClass = "ratpack.kotlin.gradle.RatpackKotlinPlugin"
    }
  }
}

// The configuration example below shows the minimum required properties
// configured to publish your plugin to the plugin portal
pluginBundle {
  website = "https://drmaas.me"
  vcsUrl = "https://github.com/drmaas/ratpack-kotlin"
  description = "Build ratpack apps with gradle more easily!"
  tags = listOf("kotlin", "ratpack", "http", "application", "web")

  (plugins) {
    "ratpackKotlinPlugin" {
      displayName = "This plugin provides build time integration for Kotlin based Ratpack applications."
    }
  }
}

task("version") {
  File(project.projectDir.toString()+"/src/main/resources", "version.txt").printWriter().use {
    it.print(project.version)
  }
}

tasks.findByName ("compileKotlin")?.dependsOn("version")
