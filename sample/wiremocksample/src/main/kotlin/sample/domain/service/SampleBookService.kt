package sample.domain.service

import mu.KotlinLogging
import ratpack.exec.Promise
import ratpack.jackson.Jackson
import sample.domain.model.Book
import javax.inject.Inject
import javax.inject.Singleton
import com.fasterxml.jackson.databind.ObjectMapper



@Singleton
class SampleBookService @Inject constructor(
    private val sampleBookRepository: SampleBookRepository
) {
    companion object {
        val log = KotlinLogging.logger {}
    }


    open fun getAllBooks(): Promise<List<Book>> {

        return Promise.value(sampleBookRepository.books)
    }

    open fun getBook(id : String): Promise<Book> {
        return Promise.value(
            sampleBookRepository.books.firstOrNull { it.id.toString() == id }
        )
    }
}
