package com.awarewire.springai.repo

import com.awarewire.springai.model.AuthorEntity
import org.springframework.data.jpa.repository.JpaRepository

interface IAuthorRepo : JpaRepository<AuthorEntity, Int> {
}