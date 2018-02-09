buildscript {
  repositories {
    mavenLocal()
    jcenter()
  }
  dependencies {
    classpath("me.drmaas:ratpack-kotlin-gradle:${project.version}")
  }
}

repositories {
  mavenLocal()
  jcenter()
}

plugins.apply("me.drmaas.ratpack-kotlin")

group = "com.example"
version = "0.0.1"

dependencies {
  runtime("ch.qos.logback:logback-classic:1.2.3")
}



