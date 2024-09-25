package com.awarewire.springai.service

import kotlin.jvm.Throws

interface ICRUD <T, ID> {

    @Throws(Exception::class)
    fun save(t: T): T

    @Throws(Exception::class)
    fun saveAll(list: List<T>): List<T>

    @Throws(Exception::class)
    fun update(t: T, id: ID): T

    @Throws(Exception::class)
    fun findAll(): List<T>

    @Throws(Exception::class)
    fun findById(id: ID): T

    @Throws(Exception::class)
    fun delete(id: ID)

}