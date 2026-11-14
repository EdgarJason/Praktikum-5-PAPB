package com.example.papb5.ui.favorites

import com.example.papb5.data.local.FavoriteStudentEntity

data class FavoritesUiState(
    val favorites: List<FavoriteStudentEntity> = emptyList(),
    val isLoading: Boolean = false
)
