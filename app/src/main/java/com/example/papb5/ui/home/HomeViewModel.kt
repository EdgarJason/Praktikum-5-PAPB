package com.example.papb5.ui.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.papb5.data.repository.StudentRepository

class HomeViewModel(
    private val repository: StudentRepository
) : ViewModel() {

    var uiState by mutableStateOf(HomeUiState())
        private set

    init {
        loadStudents()
    }

    fun loadStudents() {
        uiState = uiState.copy(isLoading = true, errorMessage = null)
        repository.getStudents { students, error ->
            if (error != null) {
                uiState = uiState.copy(isLoading = false, errorMessage = error)
            } else {
                uiState = uiState.copy(
                    isLoading = false,
                    students = students ?: emptyList(),
                    errorMessage = if (students.isNullOrEmpty()) "Tidak ada data mahasiswa" else null
                )
            }
        }
    }

    fun onSearchQueryChange(newQuery: String) {
        uiState = uiState.copy(searchQuery = newQuery)
    }

    fun searchStudents() {
        val query = uiState.searchQuery.trim()
        if (query.isEmpty()) {
            loadStudents()
        } else {
            uiState = uiState.copy(isLoading = true, errorMessage = null)
            repository.searchStudents(query) { students, error ->
                if (error != null) {
                    uiState = uiState.copy(isLoading = false, errorMessage = error)
                } else {
                    uiState = uiState.copy(
                        isLoading = false,
                        students = students ?: emptyList(),
                        errorMessage = if (students.isNullOrEmpty()) "Mahasiswa tidak ditemukan" else null
                    )
                }
            }
        }
    }

    companion object {
        fun factory(repository: StudentRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return HomeViewModel(repository) as T
                }
            }
    }
}
