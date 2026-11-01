package com.example.papb5.data.repository

import com.example.papb5.data.model.Student
import com.example.papb5.data.source.StudentRemoteDataSource

interface StudentRepository {
    fun getStudents(callback: (List<Student>?, String?) -> Unit)
    fun getStudentById(id: Int, callback: (Student?, String?) -> Unit)
    fun searchStudents(query: String, callback: (List<Student>?, String?) -> Unit)
}

class DefaultStudentRepository(
    private val remoteDataSource: StudentRemoteDataSource
) : StudentRepository {
    override fun getStudents(callback: (List<Student>?, String?) -> Unit) {
        remoteDataSource.getStudents(callback)
    }

    override fun getStudentById(id: Int, callback: (Student?, String?) -> Unit) {
        remoteDataSource.getStudentById(id, callback)
    }

    override fun searchStudents(query: String, callback: (List<Student>?, String?) -> Unit) {
        remoteDataSource.searchStudents(query, callback)
    }
}
