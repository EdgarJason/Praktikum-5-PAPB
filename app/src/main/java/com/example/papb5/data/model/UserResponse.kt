package com.example.papb5.data.model

import com.google.gson.annotations.SerializedName

data class UserResponse(
    @SerializedName("users") val users: List<UserDto>,
    @SerializedName("total") val total: Int,
    @SerializedName("skip") val skip: Int,
    @SerializedName("limit") val limit: Int
)

data class UserDto(
    @SerializedName("id") val id: Int,
    @SerializedName("firstName") val firstName: String,
    @SerializedName("lastName") val lastName: String,
    @SerializedName("company") val company: CompanyDto? = null,
    @SerializedName("university") val university: String? = null
)

data class CompanyDto(
    @SerializedName("department") val department: String? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("title") val title: String? = null
)

fun UserDto.toStudent(): Student = Student(
    id = id,
    name = "$firstName $lastName",
    program = company?.department ?: university ?: "Teknik Informatika",
    year = 2026
)
