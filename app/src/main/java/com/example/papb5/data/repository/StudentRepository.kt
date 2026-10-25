package com.example.papb5.data.repository

import com.example.papb5.data.model.Student
import com.example.papb5.data.source.StudentLocalDataSource

interface StudentRepository {
    fun getStudents(): List<Student>
    fun getStudentById(id: Int): Student?
}

class DefaultStudentRepository(
    private val dataSource: StudentLocalDataSource
) : StudentRepository {
    override fun getStudents(): List<Student> =
        dataSource.getStudents()

    override fun getStudentById(id: Int): Student? =
        dataSource.getStudentById(id)
}
