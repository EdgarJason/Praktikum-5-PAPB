package com.example.papb5.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.papb5.ui.home.HomeUiState

@Composable
fun HomeScreen(
    uiState: HomeUiState,
    onSearchQueryChange: (String) -> Unit,
    onSearchSubmit: () -> Unit,
    onRefresh: () -> Unit,
    onToggleSortOrder: () -> Unit,
    onOpenFavorites: () -> Unit,
    onOpenDetail: (Int) -> Unit,
    onOpenProfile: () -> Unit,
    onOpenAbout: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Home - Daftar Mahasiswa", style = MaterialTheme.typography.headlineMedium)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AssistChip(
                onClick = onRefresh,
                label = { Text("Refresh") }
            )
            AssistChip(
                onClick = onOpenFavorites,
                label = { Text("Favorit (Room)") }
            )
            AssistChip(
                onClick = onToggleSortOrder,
                label = { Text(if (uiState.sortAscending) "Urutan: A-Z (DataStore)" else "Urutan: Z-A (DataStore)") }
            )
            AssistChip(
                onClick = onOpenProfile,
                label = { Text("Profile") }
            )
            AssistChip(
                onClick = onOpenAbout,
                label = { Text("About") }
            )
            AssistChip(
                onClick = { onOpenDetail(9999) },
                label = { Text("Tes ID Tidak Valid") }
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = uiState.searchQuery,
                onValueChange = onSearchQueryChange,
                modifier = Modifier.weight(1f),
                placeholder = { Text("Cari nama mahasiswa...") },
                singleLine = true
            )
            Button(onClick = onSearchSubmit) {
                Text("Cari")
            }
        }

        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            uiState.errorMessage != null -> {
                Text(
                    text = uiState.errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            uiState.students.isNotEmpty() -> {
                val displayStudents = if (uiState.sortAscending) {
                    uiState.students.sortedBy { it.name }
                } else {
                    uiState.students.sortedByDescending { it.name }
                }

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(displayStudents) { student ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onOpenDetail(student.id) },
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = student.name,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(
                                    text = "ID: ${student.id} | ${student.program}",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
