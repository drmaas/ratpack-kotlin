package sample.controller

import ratpack.exec.Promise
import sample.domain.model.Book
import sample.domain.service.SampleBookService
import javax.inject.Inject

class SampleServiceController @Inject constructor(
    private val sampleBookService: SampleBookService
) {
    fun getBooks() : Promise<List<Book>> {
        return sampleBookService.getAllBooks()
    }

    fun getBookById(id : String) : Promise<Book> {
        return sampleBookService.getBook(id)
    }
}
