import ratpack.kotlin.handling.ratpack

ratpack {
  serverConfig {
    port(8080)
  }
  bindings {
    bindInstance("string")
  }
  handlers {
    get("test") {
      render("hello")
    }
  }
}
