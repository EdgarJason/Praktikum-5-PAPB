package com.example.papb5.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.papb5.data.repository.StudentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: StudentRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadStudents()
    }

    fun loadStudents() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            repository.getStudents { students, error ->
                if (error != null) {
                    _uiState.update { it.copy(isLoading = false, errorMessage = error) }
                } else {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            students = students ?: emptyList(),
                            errorMessage = if (students.isNullOrEmpty()) "Tidak ada data mahasiswa" else null
                        )
                    }
                }
            }
        }
    }

    fun onSearchQueryChange(newQuery: String) {
        _uiState.update { it.copy(searchQuery = newQuery) }
    }

    fun searchStudents() {
        val query = _uiState.value.searchQuery.trim()
        if (query.isEmpty()) {
            loadStudents()
        } else {
            viewModelScope.launch {
                _uiState.update { it.copy(isLoading = true, errorMessage = null) }
                repository.searchStudents(query) { students, error ->
                    if (error != null) {
                        _uiState.update { it.copy(isLoading = false, errorMessage = error) }
                    } else {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                students = students ?: emptyList(),
                                errorMessage = if (students.isNullOrEmpty()) "Mahasiswa tidak ditemukan" else null
                            )
                        }
                    }
                }
            }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val query = _uiState.value.searchQuery.trim()
            if (query.isEmpty()) {
                repository.getStudents { students, error ->
                    if (error != null) {
                        _uiState.update { it.copy(isLoading = false, errorMessage = error) }
                    } else {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                students = students ?: emptyList(),
                                errorMessage = if (students.isNullOrEmpty()) "Tidak ada data mahasiswa" else null
                            )
                        }
                    }
                }
            } else {
                repository.searchStudents(query) { students, error ->
                    if (error != null) {
                        _uiState.update { it.copy(isLoading = false, errorMessage = error) }
                    } else {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                students = students ?: emptyList(),
                                errorMessage = if (students.isNullOrEmpty()) "Mahasiswa tidak ditemukan" else null
                            )
                        }
                    }
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
