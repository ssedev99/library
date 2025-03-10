package com.example.library.model

import kotlinx.serialization.Serializable

@Serializable
data class BooksResponse(
    val items: List<BookItem>?
)

@Serializable
data class BookItem(
    val id: String,
    val selfLink: String,
    val volumeInfo: VolumeInfo,
)

@Serializable
data class VolumeInfo(
    val title: String,
    val authors: List<String>,
    val imageLinks: ImageLink
)

@Serializable
data class ImageLink(
    val smallThumbnail: String,
    val thumbnail: String
)