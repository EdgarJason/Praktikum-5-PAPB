package com.example.papb5.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.papb5.data.repository.FavoriteRepository
import com.example.papb5.data.repository.StudentRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DetailViewModel(
    private val repository: StudentRepository,
    private val favoriteRepository: FavoriteRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetailUiState())
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    private var favoriteJob: Job? = null

    fun loadStudent(id: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            repository.getStudentById(id) { student, error ->
                if (error != null) {
                    _uiState.update { DetailUiState(isLoading = false, errorMessage = error) }
                } else if (student != null) {
                    _uiState.update { DetailUiState(isLoading = false, student = student) }
                    observeFavorite(id)
                } else {
                    _uiState.update { DetailUiState(isLoading = false, errorMessage = "Student tidak ditemukan") }
                }
            }
        }
    }

    private fun observeFavorite(id: Int) {
        favoriteJob?.cancel()
        favoriteJob = viewModelScope.launch {
            favoriteRepository.observeIsFavorite(id).collect { isFav ->
                _uiState.update { it.copy(isFavorite = isFav) }
            }
        }
    }

    fun toggleFavorite() {
        val student = uiState.value.student ?: return
        viewModelScope.launch {
            if (uiState.value.isFavorite) {
                favoriteRepository.remove(student.id)
            } else {
                favoriteRepository.add(student)
            }
        }
    }

    companion object {
        fun factory(
            repository: StudentRepository,
            favoriteRepository: FavoriteRepository
        ): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return DetailViewModel(repository, favoriteRepository) as T
                }
            }
    }
}
