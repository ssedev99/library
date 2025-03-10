package com.example.library.data

import com.example.library.model.BookItem
import com.example.library.network.LibraryApiService

interface LibraryRepository {
    suspend fun getBooks(query: String, startIndex: Int): List<BookItem>
}

class NetworkMarsPhotosRepository(
    private val libraryApiService: LibraryApiService,
) : LibraryRepository {
    override suspend fun getBooks(query: String, startIndex: Int): List<BookItem> {
        return libraryApiService.getBooks(query, startIndex).items ?: emptyList()
    }
}