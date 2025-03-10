package com.example.library.data

import com.example.library.network.LibraryApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

interface AppContainer {
    val libraryRepository: LibraryRepository
}

class DefaultAppContainer : AppContainer {

    private val baseUrl = "https://www.googleapis.com/books/v1/"

    private val json = Json { ignoreUnknownKeys = true }

    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(baseUrl)
        .build()

    private val retrofitService: LibraryApiService by lazy {
        retrofit.create(LibraryApiService::class.java)
    }

    override val libraryRepository: LibraryRepository by lazy {
        NetworkMarsPhotosRepository(retrofitService)
    }

}