package com.awarewire.springai.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class AuthorBookDTO @JsonCreator constructor(
    @JsonProperty("author") val author: String = "",
    @JsonProperty("books") val books: List<String> = emptyList()
)
