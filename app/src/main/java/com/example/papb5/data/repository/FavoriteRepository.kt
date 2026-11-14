package com.example.papb5.data.repository

import com.example.papb5.data.local.FavoriteStudentDao
import com.example.papb5.data.local.FavoriteStudentEntity
import com.example.papb5.data.model.Student
import kotlinx.coroutines.flow.Flow

class FavoriteRepository(
    private val dao: FavoriteStudentDao
) {
    fun observeIsFavorite(id: Int): Flow<Boolean> =
        dao.observeIsFavorite(id)

    fun observeFavorites(): Flow<List<FavoriteStudentEntity>> =
        dao.observeFavorites()

    suspend fun add(student: Student) {
        dao.insert(
            FavoriteStudentEntity(
                id = student.id,
                firstName = student.name,
                lastName = "",
                email = "${student.name.lowercase().replace(" ", "")}@student.ub.ac.id"
            )
        )
    }

    suspend fun remove(id: Int) {
        dao.deleteById(id)
    }
}
