package com.example.library.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.library.LibraryApplication
import com.example.library.data.LibraryRepository
import com.example.library.model.BookItem
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed interface LibraryUiState {
    data class Success(val books: List<BookItem>) : LibraryUiState
    object Error : LibraryUiState
    object Loading : LibraryUiState
}

class LibraryViewModel(private val libraryRepository: LibraryRepository) : ViewModel() {
    var libraryUiState: LibraryUiState by mutableStateOf(LibraryUiState.Loading)
        private set

    init {
        getBooks()
    }

    fun getBooks(loadMore: Boolean = false) {
        val query: String = "jazz+history"

        viewModelScope.launch {
            if (!loadMore) {
                libraryUiState = LibraryUiState.Loading
            }

            libraryUiState = try {
                val currentList = (libraryUiState as? LibraryUiState.Success)?.books ?: emptyList()
                val startIndex = currentList.size
                val newBooks = libraryRepository.getBooks(query, startIndex)
                LibraryUiState.Success(currentList.plus(newBooks))
            } catch (e: IOException) {
                LibraryUiState.Error
            } catch (e: HttpException) {
                LibraryUiState.Error
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as LibraryApplication)
                val libraryRepository = application.container.libraryRepository
                LibraryViewModel(libraryRepository = libraryRepository)
            }
        }
    }
}

