plugins {
  id("me.drmaas.ratpack-kotlin") version "1.6.0-dev.0.uncommitted+58b87d9"
  id("com.github.johnrengelman.shadow") version "4.0.2"
}

repositories {
  mavenLocal()
  jcenter()
}

dependencies {
  runtime("ch.qos.logback:logback-classic:1.2.3")
}

group = "com.example"
version = "0.0.1"
