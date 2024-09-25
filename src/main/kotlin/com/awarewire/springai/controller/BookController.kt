package com.awarewire.springai.controller

import com.awarewire.springai.model.BookEntity
import com.awarewire.springai.service.IBookService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI
import kotlin.jvm.Throws

@RestController
@RequestMapping("/books")
@CrossOrigin(origins = ["*"])
class BookController(private val service: IBookService) {

    @GetMapping
    @Throws(Exception::class)
    fun findAll(): ResponseEntity<List<BookEntity>> {
        return ResponseEntity.ok(service.findAll())
    }

    @GetMapping("/{id}")
    @Throws(Exception::class)
    fun findById(@PathVariable("id") id: Int): ResponseEntity<BookEntity> {
        return ResponseEntity.ok(service.findById(id))
    }

    @PostMapping
    fun save(@RequestBody book: BookEntity, request: HttpServletRequest): ResponseEntity<BookEntity> {
        val createdBook = service.save(book)
        return ResponseEntity.created(URI.create("${request.requestURI}${createdBook.idBook}")).body(createdBook)
    }

    @PutMapping("/{id}")
    fun update(@PathVariable("id") id: Int, @RequestBody book: BookEntity): ResponseEntity<BookEntity> {
        return ResponseEntity.ok(service.update(book, id))
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable("id") id: Int): ResponseEntity<Any> {
        service.delete(id)
        return ResponseEntity.noContent().build()
    }
}