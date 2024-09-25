package com.awarewire.springai.dto

data class ResponseDTO<T>(
    val status: Int,
    val message: String,
    val data: T
)
