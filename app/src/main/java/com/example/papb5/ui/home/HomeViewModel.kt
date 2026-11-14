package com.example.papb5.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.papb5.data.preferences.UserPreferencesRepository
import com.example.papb5.data.repository.StudentRepository
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class HomeViewModel(
    private val repository: StudentRepository,
    private val preferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val initialQuery = preferencesRepository.lastSearchQuery.first()
            _uiState.update { it.copy(searchQuery = initialQuery) }
            if (initialQuery.isNotEmpty()) {
                searchStudents()
            } else {
                loadStudents()
            }
        }

        viewModelScope.launch {
            _uiState
                .debounce(500)
                .distinctUntilChanged { old, new -> old.searchQuery == new.searchQuery }
                .collect { state ->
                    preferencesRepository.saveLastSearchQuery(state.searchQuery)
                }
        }

        viewModelScope.launch {
            preferencesRepository.sortAscending.collect { isAscending ->
                _uiState.update { it.copy(sortAscending = isAscending) }
            }
        }
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

    fun toggleSortOrder() {
        viewModelScope.launch {
            val current = uiState.value.sortAscending
            preferencesRepository.saveSortAscending(!current)
        }
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
        fun factory(
            repository: StudentRepository,
            preferencesRepository: UserPreferencesRepository
        ): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return HomeViewModel(repository, preferencesRepository) as T
                }
            }
    }
}
