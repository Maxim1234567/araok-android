package ru.araok.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ru.araok.entites.User
import java.time.LocalDate

@JsonClass(generateAdapter = true)
data class UserDto(
    @Json(name = "id") override val id: Long,
    @Json(name = "name") override val name: String,
    @Json(name = "phone") override val phone: String,
    @Json(name = "password") override val password: String,
    @Json(name = "birthDate") override val birthDate: LocalDate,
    @Json(name = "role") override val role: String
): User
