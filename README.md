# ratpack-kotlin

[![Build Status](https://travis-ci.org/drmaas/ratpack-kotlin.svg?branch=master)](https://travis-ci.org/drmaas/ratpack-kotlin)

[![forthebadge](https://forthebadge.com/images/badges/uses-badges.svg)](https://forthebadge.com)

Build fluent kotlin ratpack applications. Features:
* Server, Chain, and Handler DSL
* RxJava extension functions via [RxRatpackKotlin.kt]

## Usage

Sample App.kt:
```
fun main(args: Array<String>) {
  ratpack {
    serverConfig {
      development(true)
      port(9000)
    }
    bindings {
      bindInstance("foobar")
    }
    handlers {
      get("test") {
        render("hello " + context.get(String::class.java))
      }
    }
  }
}
```
```
curl localhost:9000/test
```

The kotlin dsl library is available via maven central:

Gradle:

```groovy
compile 'me.drmaas:ratpack-kotlin-dsl:x.x.x'
```

Maven:

```xml
<dependency>
  <groupId>me.drmaas</groupId>
  <artifactId>ratpack-kotlin-dsl</artifactId>
  <version>x.x.x</version>
</dependency>
```

## Testing ratpack kotlin apps

A small test library is also published. This provides a convenient way to spin up ratpack apps in test cases.
```
    // kotlintest example
    given("a ratpack server") {
      val app = ratpack {
        handlers {
          get("test") {
            render("hello")
          }
        }
      }
      `when`("a request is made") {
        val client = testHttpClient(app)
        val r = client.get("test")
        then("it works") {
          r.body.text shouldEqual "hello"
          app.stop()
        }
      }
    }
```

Gradle:

```groovy
compile 'me.drmaas:ratpack-kotlin-test:x.x.x'
```

Maven:

```xml
<dependency>
  <groupId>me.drmaas</groupId>
  <artifactId>ratpack-kotlin-test</artifactId>
  <version>x.x.x</version>
</dependency>
```
