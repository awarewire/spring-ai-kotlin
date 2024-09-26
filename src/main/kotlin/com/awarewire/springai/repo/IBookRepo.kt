package com.awarewire.springai.repo

import com.awarewire.springai.model.BookEntity
import org.springframework.data.jpa.repository.JpaRepository

interface IBookRepo : JpaRepository<BookEntity, Int> {

    fun findByNameLike(name: String): List<BookEntity>
}