package sample


import config.SampleServerConfigurer
import config.SourceConfig
import mu.KotlinLogging
import ratpack.kotlin.handling.ratpack
import ratpack.server.ServerConfig
import ratpack.server.ServerConfigBuilder
import ratpack.service.Service
import ratpack.service.StartEvent
import sample.application.ApplicationModule
import sample.http.NotFoundChain
import sample.http.SampleServiceChain
import javax.inject.Inject
import javax.inject.Singleton


fun main() {
    app()
}

fun app(bindings: List<Any> = listOf()) = ratpack {
    System.setProperty("log4j2.contextSelector", "org.apache.logging.log4j.core.async.AsyncLoggerContextSelector")

    serverConfig {
        ApplicationConfigurer.config(this)
    }

    bindings {
        for (b in bindings) {
            bindInstance(b)
        }
        module(ApplicationModule(serverConfig))
        bind(StartupService::class.java)
    }

    handlers {
        prefix("books", SampleServiceChain::class.java)
        all(chain(NotFoundChain::class.java))
    }
}

object ApplicationConfigurer {
    fun config(builder: ServerConfigBuilder) : ServerConfigBuilder {
        return SampleServerConfigurer.config(builder)
            .env("SAMPLE__")
    }

}

@Singleton
class StartupService @Inject constructor(val serverConfig: ServerConfig) : Service {
    val logger = KotlinLogging.logger {StartupService::class.java}
    override fun onStart(event: StartEvent?) {
        logger.info {
            "STARTED"
        }
    }
}
