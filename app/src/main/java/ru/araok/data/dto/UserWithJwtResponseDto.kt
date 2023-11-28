package ru.araok.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ru.araok.entites.JwtResponse
import ru.araok.entites.User
import ru.araok.entites.UserWithJwtResponse

@JsonClass(generateAdapter = true)
data class UserWithJwtResponseDto constructor(
    @Json(name = "user") override val user: User,
    @Json(name = "token") override val token: JwtResponse
): UserWithJwtResponse