package sample.http

import ratpack.kotlin.handling.KChainAction
import javax.inject.Inject

class NotFoundChain @Inject constructor() : KChainAction() {
    override fun execute() {
        path("") {
            render("NOPE")
        }
    }
}
