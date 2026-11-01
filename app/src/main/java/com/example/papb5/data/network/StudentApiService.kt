package com.example.papb5.data.network

import com.example.papb5.data.model.UserDto
import com.example.papb5.data.model.UserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface StudentApiService {
    @GET("users")
    fun getUsers(): Call<UserResponse>

    @GET("users/{id}")
    fun getUserById(@Path("id") id: Int): Call<UserDto>

    @GET("users/search")
    fun searchUsers(@Query("q") query: String): Call<UserResponse>
}
