package com.awarewire.springai

import com.awarewire.springai.model.AuthorEntity
import com.awarewire.springai.model.BookEntity
import com.awarewire.springai.service.IAuthorService
import com.awarewire.springai.service.IBookService
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.core.io.ResourceLoader
import org.springframework.util.FileCopyUtils
import java.nio.charset.StandardCharsets

@SpringBootApplication
class SpringAiApplication(
    private val resourceLoader: ResourceLoader,
    private val authorService: IAuthorService,
    private val bookService: IBookService
): ApplicationRunner {

    @Throws(Exception::class)
    override fun run(args: ApplicationArguments?) {
        val resourceAuthor = resourceLoader.getResource("classpath:json/authors.json")
        val resourceBook = resourceLoader.getResource("classpath:json/books.json")

        val jsonData1: ByteArray = FileCopyUtils.copyToByteArray(resourceAuthor.inputStream)
        val jsonData2: ByteArray = FileCopyUtils.copyToByteArray(resourceBook.inputStream)

        val jsonStringAuthors = String(jsonData1, StandardCharsets.UTF_8)
        val jsonStringBooks = String(jsonData2, StandardCharsets.UTF_8)

        val gson = Gson()
        val authorListType = object : TypeToken<List<AuthorEntity>>() {}.type
        val bookListType = object : TypeToken<List<BookEntity>>() {}.type

        val authors: List<AuthorEntity> = gson.fromJson(jsonStringAuthors, authorListType)
        val books: List<BookEntity> = gson.fromJson(jsonStringBooks, bookListType)

        authorService.saveAll(authors)
        bookService.saveAll(books)
    }

}

fun main(args: Array<String>) {
    runApplication<SpringAiApplication>(*args)
}
