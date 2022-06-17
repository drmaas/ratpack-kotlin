// First, apply the publishing plugin
plugins {
  id("com.gradle.plugin-publish") version "0.21.0"
  `java-gradle-plugin`
}

repositories {
  mavenCentral()
}

apply {
  from ("../dependencies.gradle")
}

dependencies {
  // other dependencies that your plugin requires
  implementation("io.ratpack:ratpack-gradle:${(project.ext["commonVersions"] as Map<String, String>)["ratpack"]}")
  implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:${(project.ext["commonVersions"] as Map<String, String>)["kotlin"]}")

  testImplementation("io.kotest:kotest-runner-junit5-jvm:${(project.ext["commonVersions"] as Map<String, String>)["kotlinTest"]}")
  testImplementation("io.kotest:kotest-assertions-core-jvm:${(project.ext["commonVersions"] as Map<String, String>)["kotlinTest"]}")
  testImplementation("ch.qos.logback:logback-classic:${(project.ext["commonVersions"] as Map<String, String>)["logback"]}")
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
