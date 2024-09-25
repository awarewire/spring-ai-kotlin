package com.awarewire.springai.model

import jakarta.persistence.*

@Entity
@Table(name = "author")
data class AuthorEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var idAuthor: Int = 0,

    @Column(nullable = false, length = 50)
    var firstName: String = "",

    @Column(nullable = false, length = 50)
    var lastName: String = "",

    @Column(nullable = false, length = 35)
    var country: String = "",

    @Column(nullable = false, length = 500)
    var urlPhoto: String = ""
)
