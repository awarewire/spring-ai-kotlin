package com.awarewire.springai.controller

import com.awarewire.springai.model.AuthorEntity
import com.awarewire.springai.service.IAuthorService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI
import kotlin.jvm.Throws

@RestController
@RequestMapping("/authors")
@CrossOrigin(origins = ["*"])
class AuthorController (private val service: IAuthorService) {

    @GetMapping
    @Throws(Exception::class)
    fun findAll(): ResponseEntity<List<AuthorEntity>> {
        return ResponseEntity.ok(service.findAll())
    }

    @GetMapping("/{id}")
    @Throws(Exception::class)
    fun findById(@PathVariable("id") id: Int): ResponseEntity<AuthorEntity> {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    @Throws(Exception::class)
    fun save(@RequestBody author: AuthorEntity): ResponseEntity<AuthorEntity> {
        val createdAuthor = service.save(author)
        return ResponseEntity.created(URI.create("http://localhost:8080/api/v1/authors/${createdAuthor.idAuthor}")).body(createdAuthor)
        //return ResponseEntity.status(HttpStatus.CREATED).body(createdAuthor)
    }

    @PutMapping("/{id}")
    @Throws(Exception::class)
    fun update(@PathVariable("id") id: Int, @RequestBody author: AuthorEntity): ResponseEntity<AuthorEntity> {
        return ResponseEntity.ok(service.update(author, id))
    }

    @DeleteMapping("/{id}")
    @Throws(Exception::class)
    fun deleteById(@PathVariable("id") id: Int): ResponseEntity<Unit> {
        service.delete(id)
        return ResponseEntity.noContent().build()
    }

}