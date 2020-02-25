package ratpack.kotlin.runner

import javax.script.ScriptEngineManager

class KotlinDslRunner {
  companion object {
    @JvmStatic
    fun main(args: Array<String>) {
      val engine = ScriptEngineManager().getEngineByExtension("kts")
      val reader = KotlinDslRunner::class.java.classLoader.getResourceAsStream("ratpack.kts").bufferedReader()
      engine.eval(reader)
    }
  }
}

