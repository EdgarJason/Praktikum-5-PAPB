package com.example.papb5.ui.location

import com.example.papb5.data.location.DeviceLocation

sealed interface LocationUiState {
    data object Idle : LocationUiState
    data object Loading : LocationUiState
    data class Success(val location: DeviceLocation) : LocationUiState
    data class Error(val message: String) : LocationUiState
    data class PermissionDenied(val message: String) : LocationUiState
}
