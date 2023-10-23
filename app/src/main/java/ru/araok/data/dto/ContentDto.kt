package ru.araok.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ru.araok.entites.Content
import java.time.LocalDate

@JsonClass(generateAdapter = true)
data class ContentDto(
    @Json(name = "id") override val id: Long?,
    @Json(name = "name") override val name: String,
    @Json(name = "limit") override val limit: AgeLimitDto,
    @Json(name = "artist") override val artist: String,
    @Json(name = "user") override val user: UserDto,
    @Json(name = "createDate") override val createDate: LocalDate,
    @Json(name = "language") override val language: LanguageDto
): Content
