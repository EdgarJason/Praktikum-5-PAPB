package com.example.papb5.data.source

import com.example.papb5.data.model.Student
import com.example.papb5.data.model.UserDto
import com.example.papb5.data.model.UserResponse
import com.example.papb5.data.model.toStudent
import com.example.papb5.data.network.StudentApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StudentRemoteDataSource(
    private val apiService: StudentApiService
) {
    fun getStudents(callback: (List<Student>?, String?) -> Unit) {
        apiService.getUsers().enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    val students = response.body()?.users?.map { it.toStudent() } ?: emptyList()
                    callback(students, null)
                } else {
                    callback(null, "Gagal mengambil data (${response.code()})")
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                callback(null, t.message ?: "Koneksi gagal")
            }
        })
    }

    fun getStudentById(id: Int, callback: (Student?, String?) -> Unit) {
        apiService.getUserById(id).enqueue(object : Callback<UserDto> {
            override fun onResponse(call: Call<UserDto>, response: Response<UserDto>) {
                if (response.isSuccessful) {
                    val student = response.body()?.toStudent()
                    callback(student, null)
                } else {
                    callback(null, "Student tidak ditemukan")
                }
            }

            override fun onFailure(call: Call<UserDto>, t: Throwable) {
                callback(null, t.message ?: "Koneksi gagal")
            }
        })
    }

    fun searchStudents(query: String, callback: (List<Student>?, String?) -> Unit) {
        apiService.searchUsers(query).enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    val students = response.body()?.users?.map { it.toStudent() } ?: emptyList()
                    callback(students, null)
                } else {
                    callback(null, "Gagal mencari data (${response.code()})")
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                callback(null, t.message ?: "Koneksi gagal")
            }
        })
    }
}
