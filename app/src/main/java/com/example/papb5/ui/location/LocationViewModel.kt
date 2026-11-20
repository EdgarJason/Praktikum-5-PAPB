package com.example.papb5.ui.location

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.papb5.data.location.LocationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LocationViewModel(
    private val repository: LocationRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<LocationUiState>(LocationUiState.Idle)
    val uiState: StateFlow<LocationUiState> = _uiState.asStateFlow()

    fun refreshLocation() {
        viewModelScope.launch {
            _uiState.value = LocationUiState.Loading
            _uiState.value = repository.getCurrentLocation().fold(
                onSuccess = { LocationUiState.Success(it) },
                onFailure = { LocationUiState.Error(it.message ?: "Gagal mengambil lokasi") }
            )
        }
    }

    fun onPermissionDenied() {
        _uiState.value = LocationUiState.PermissionDenied("Izin lokasi ditolak. Tekan 'Minta Izin Lagi' untuk memberikan akses lokasi.")
    }

    companion object {
        fun factory(repository: LocationRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return LocationViewModel(repository) as T
                }
            }
    }
}
