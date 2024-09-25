package com.awarewire.springai.model

import jakarta.persistence.*

@Entity
@Table(name = "book")
data class BookEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var idBook: Int = 0,

    @Column(nullable = false, length = 50)
    var name: String = "",

    @Column(nullable = false, length = 2000)
    var review: String = "",

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    var author: AuthorEntity = AuthorEntity(),

    @Column(nullable = false, length = 500)
    var urlCover: String = "",

    @Column(nullable = false)
    var enable: Boolean = false
)
