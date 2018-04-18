plugins {
  id("me.drmaas.ratpack-kotlin") version "1.3.4"
  id("com.github.johnrengelman.shadow") version "2.0.2"
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
