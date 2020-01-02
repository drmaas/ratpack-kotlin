package sample.http

import io.netty.handler.codec.http.HttpResponseStatus
import ratpack.jackson.Jackson
import ratpack.kotlin.handling.KChainAction
import sample.controller.SampleServiceController
import java.security.InvalidParameterException
import javax.inject.Inject

class SampleServiceChain @Inject constructor(
    private val sampleServerController: SampleServiceController
) : KChainAction() {
    override fun execute() {
        path("") {
            byMethod {
                get {
                    byContent {
                        json {
                            sampleServerController.getBooks().then {
                                response.status(HttpResponseStatus.OK.code())
                                render(Jackson.json(it))
                            }
                        }
                    }
                }
            }
        }
        path(":id") {
            byMethod {
                get {
                    val pathValue = context.pathTokens["id"].orEmpty()
                    if (pathValue.isEmpty()) {
                        throw InvalidParameterException("id")
                    }
                    byContent {
                        json {
                            sampleServerController.getBookById(pathValue).then {
                                response.status(HttpResponseStatus.OK.code())
                                render(Jackson.json(it))
                            }
                        }
                    }
                }
            }
        }
    }
}
