package ratpack.kotlin.handling

import kotlin.jvm.functions.Function1
import ratpack.kotlin.RatpackKotlinDslSpec

/**
 *
 */
class SmokeSpec extends RatpackKotlinDslSpec {

    def "simple test"() {
        when:
        serverConfig {
            port(8080)
        }
        bindings {
            add('OK')
        }
        handlers {
            get {
                it.render it.get(String)
            }
        }

        then:
        text == 'OK'
    }
}
