package com.awarewire.springai.service.impl

import com.awarewire.springai.model.BookEntity
import com.awarewire.springai.repo.IBookRepo


class BookFunctionServiceImpl(private val repo: IBookRepo) : (Request) -> Response {

    override fun invoke(request: Request): Response {
        val books = repo.findByNameLike("%${request.bookName}%")
            .map { BookDTO.fromEntity(it) }
        return Response(books)
    }
}

data class Request(val bookName: String)

data class Response(val books: List<BookDTO>)

data class BookDTO(val id: Long, val name: String, val author: String) {
    companion object {
        fun fromEntity(entity: BookEntity): BookDTO {
            return BookDTO(entity.idBook.toLong(), entity.name, entity.author.firstName + " " + entity.author.lastName)
        }
    }
}