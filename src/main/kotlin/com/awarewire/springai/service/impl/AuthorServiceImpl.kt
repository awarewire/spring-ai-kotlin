package com.awarewire.springai.service.impl

import com.awarewire.springai.model.AuthorEntity
import com.awarewire.springai.repo.IAuthorRepo
import com.awarewire.springai.service.IAuthorService
import org.springframework.stereotype.Service

@Service
class AuthorServiceImpl(private val repo: IAuthorRepo): IAuthorService {

    override fun save(t: AuthorEntity): AuthorEntity {
        return repo.save(t)
    }

    override fun saveAll(list: List<AuthorEntity>): List<AuthorEntity> {
        return repo.saveAll(list)
    }

    override fun update(t: AuthorEntity, id: Int): AuthorEntity {
        return repo.save(t)
    }

    override fun findAll(): List<AuthorEntity> {
        return repo.findAll()
    }

    override fun findById(id: Int): AuthorEntity {
        return repo.findById(id).orElse(AuthorEntity())
    }

    override fun delete(id: Int) {
        repo.deleteById(id)
    }

}