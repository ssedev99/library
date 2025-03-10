package com.example.library.network

import com.example.library.model.BooksResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface LibraryApiService {
    @GET("volumes")
    suspend fun getBooks(
        @Query("q") query: String,
        @Query("startIndex") startIndex: Int
    ): BooksResponse
}