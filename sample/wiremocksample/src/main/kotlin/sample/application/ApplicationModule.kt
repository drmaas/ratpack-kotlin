package sample.application

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.google.inject.AbstractModule
import com.google.inject.Scopes
import ratpack.server.ServerConfig
import sample.controller.SampleServiceController
import sample.domain.service.SampleBookService
import sample.http.NotFoundChain
import sample.http.SampleServiceChain

class ApplicationModule constructor(val serverConfig: ServerConfig) : AbstractModule() {
    override fun configure() {
        bind(SampleBookService::class.java).`in`(Scopes.SINGLETON)

        bind(SampleServiceController::class.java).`in`(Scopes.SINGLETON)

        bind(SampleServiceChain::class.java).`in`(Scopes.SINGLETON)
        bind(NotFoundChain::class.java).`in`(Scopes.SINGLETON)


//        bind(ObjectMapper::class.java).toInstance(ObjectMapper().registerModule(KotlinModule()))
    }
}
