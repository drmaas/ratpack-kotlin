import com.example.TestChain
import ratpack.kotlin.handling.ratpack

ratpack {
  serverConfig {
    port(8080)
  }
  bindings {
    bindInstance("string")
    bind(TestChain::class.java)
  }
  handlers {
    get("test") {
      render(get(String::class.java))
    }
    prefix("other", TestChain::class.java)
  }
}
