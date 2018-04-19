// First, apply the publishing plugin
plugins {
  id("com.gradle.plugin-publish") version "0.9.10"

  // Apply other plugins here, e.g. java plugin for a plugin written in java or
  // the kotlin plugin for a plugin written in kotlin
}

repositories {
  jcenter()
}

apply {
  from ("../dependencies.gradle")
  plugin(JavaGradlePluginPlugin::class.java)
}

dependencies {
  compile(gradleApi())
  // other dependencies that your plugin requires
  compile("io.ratpack:ratpack-gradle:${(ext["commonVersions"] as Map<String, String>)["ratpack"]}")
  compile("com.netflix.nebula:nebula-kotlin-plugin:${(ext["commonVersions"] as Map<String, String>)["kotlin"]}")

  testCompile("io.kotlintest:kotlintest:${(ext["commonVersions"] as Map<String, String>)["kotlinTest"]}")
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
      id = "me.drmaas.ratpack-kotlin"
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
