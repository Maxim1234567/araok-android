package ru.araok.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ru.araok.entites.MediaSubtitle

@JsonClass(generateAdapter = true)
data class MediaSubtitleDto(
    @Json(name = "id") override val id: Long?,
    @Json(name = "content") override val content: ContentDto,
    @Json(name = "language") override val language: LanguageDto,
    @Json(name = "subtitles") override val subtitles: List<SubtitleDto>
): MediaSubtitle
