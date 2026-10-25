package com.example.papb5.data.source

import com.example.papb5.data.model.Student

class StudentLocalDataSource {
    private val students = listOf(
        Student(2026, "Alya Putri", "Teknik Informatika", 2026),
        Student(2027, "Bima Pratama", "Teknik Informatika", 2026),
        Student(2028, "Citra Lestari", "Teknik Informatika", 2026),
        Student(2029, "Dian Sastro", "Sistem Informasi", 2025),
        Student(2030, "Eko Wijaya", "Pendidikan Teknologi Informasi", 2025)
    )

    fun getStudents(): List<Student> = students

    fun getStudentById(id: Int): Student? =
        students.find { it.id == id }
}
