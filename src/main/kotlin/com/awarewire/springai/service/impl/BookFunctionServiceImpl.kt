package com.awarewire.springai.service.impl

import com.awarewire.springai.model.BookEntity
import com.awarewire.springai.repo.IBookRepo


class BookFunctionServiceImpl(private val repo: IBookRepo) : (BookFunctionServiceImpl.Request) -> BookFunctionServiceImpl.Response {

    data class Request(val bookName: String)
    data class Response(val books: List<BookEntity>)

    override fun invoke(request: Request): Response {
        val books = repo.findByNameLike("%${request.bookName}%")
        return Response(books)
    }
}