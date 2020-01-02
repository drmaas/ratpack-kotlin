package sample.domain.service


import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import sample.domain.model.Book
import javax.inject.Singleton

@Singleton
class SampleBookRepository {
    val mapper = jacksonObjectMapper()
    val json = """[
    {
      "id": "100",
      "name": "Catch-22",
      "year": "1961",
      "author": "Joseph Heller"
    },
    {
      "id": "101",
      "name": "The Great Gatsby",
      "year": "1925",
      "author": "F. Scott Fitzgerald"
    },
    {
      "id": "102",
      "name": "Beloved",
      "year": "1987",
      "author": "Toni Morrison"
    },
    {
      "id": "103",
      "name": "Things Fall About",
      "year": "1958",
      "author": "Chinua Achebe"
    },
    {
      "id": "104",
      "name": "Catching Fire",
      "year": "2009",
      "author": "Suzanne Collins"
    }
  ]"""

 val books : List<Book> = mapper.readValue(json)

}
