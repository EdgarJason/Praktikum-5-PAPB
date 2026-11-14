package com.example.papb5.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.papb5.ui.favorites.FavoritesUiState

@Composable
fun FavoritesScreen(
    uiState: FavoritesUiState,
    onOpenDetail: (Int) -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Mahasiswa Favorit (Room)", style = MaterialTheme.typography.headlineMedium)

        if (uiState.favorites.isEmpty()) {
            Text(
                text = "Belum ada mahasiswa favorit tersimpan.",
                style = MaterialTheme.typography.bodyLarge
            )
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(uiState.favorites) { favorite ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onOpenDetail(favorite.id) },
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = favorite.firstName,
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = "ID: ${favorite.id} | Email: ${favorite.email}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }

        Button(onClick = onBack) {
            Text("Kembali")
        }
    }
}
