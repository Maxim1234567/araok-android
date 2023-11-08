package ru.araok.domain

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import ru.araok.consts.TypeContent
import ru.araok.data.AraokRepository
import ru.araok.data.dto.ContentWithContentMediaAndMediaSubtitleDto
import ru.araok.entites.AgeLimit
import ru.araok.entites.Content
import javax.inject.Inject

class GetAraokUseCase @Inject constructor(
    private val araokRepository: AraokRepository
) {
    //age limit
    suspend fun getAgeLimit() = araokRepository.getAgeLimit()

    //content
    suspend fun getContents(type: TypeContent) = araokRepository.getContents(type)

    suspend fun getContentsByName(name: String) = araokRepository.getContentsByName(name)

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getContentById(id: Long) = araokRepository.getContentById(id)

    suspend fun contentSave(content: ContentWithContentMediaAndMediaSubtitleDto) = araokRepository.contentSave(content)

    //language
    suspend fun gtAllLanguages() = araokRepository.getAllLanguages()

    //media subtitle
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getSubtitle(contentId: Long, languageId: Long) = araokRepository.getSubtitle(contentId, languageId)

    //media
    suspend fun getMedia(contentId: Long, typeId: Long = 1) = araokRepository.getMedia(contentId, typeId)
}