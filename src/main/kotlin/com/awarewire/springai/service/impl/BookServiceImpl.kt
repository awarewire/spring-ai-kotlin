package com.awarewire.springai.service.impl

import com.awarewire.springai.model.BookEntity
import com.awarewire.springai.repo.IBookRepo
import com.awarewire.springai.service.IBookService
import org.springframework.stereotype.Service

@Service
class BookServiceImpl(private val repo: IBookRepo) : IBookService {

    override fun save(t: BookEntity): BookEntity {
        return repo.save(t)
    }

    override fun saveAll(list: List<BookEntity>): List<BookEntity> {
        return repo.saveAll(list)
    }

    override fun update(t: BookEntity, id: Int): BookEntity {
        return repo.save(t)
    }

    override fun findAll(): List<BookEntity> {
        return repo.findAll()
    }

    override fun findById(id: Int): BookEntity {
        return repo.findById(id).orElse(BookEntity())
    }

    override fun delete(id: Int) {
        repo.deleteById(id)
    }
}