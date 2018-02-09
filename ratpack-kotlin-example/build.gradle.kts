//buildscript {
//  repositories {
//    mavenLocal()
//    jcenter()
//  }
//  dependencies {
//    classpath("me.drmaas:ratpack-kotlin-gradle:1.2.0-rc.2")
//  }
//}
plugins {
  id("me.drmaas.ratpack-kotlin") version "1.2.0-rc.2"
}

repositories {
  mavenLocal()
  jcenter()
}

//plugins.apply("me.drmaas.ratpack-kotlin")

dependencies {
  runtime("ch.qos.logback:logback-classic:1.2.3")
}

group = "com.example"
version = "0.0.1"
