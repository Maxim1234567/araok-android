package ru.araok.domain

import android.os.Build
import androidx.annotation.RequiresApi
import ru.araok.consts.TypeContent
import ru.araok.data.AraokRepository
import ru.araok.data.dto.*
import ru.araok.entites.AgeLimit
import ru.araok.entites.Content
import ru.araok.entites.JwtRequest
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

    suspend fun getAllLanguageSubtitle(contentId: Long) = araokRepository.getAllLanguageSubtitle(contentId)

    //media subtitle
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getSubtitle(contentId: Long, languageId: Long) = araokRepository.getSubtitle(contentId, languageId)

    //media
    suspend fun getMedia(contentId: Long, typeId: Long = 1) =
        araokRepository.getMedia(contentId, typeId)

    //setting
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getSetting(contentId: Long) =
        araokRepository.getSetting(contentId)

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun saveSetting(settings: SettingsDto) =
        araokRepository.saveSetting(settings)

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun updateSetting(settings: SettingsDto) =
        araokRepository.updateSetting(settings)

    //authorization
    suspend fun login(jwtRequest: JwtRequestDto) =
        araokRepository.login(jwtRequest)

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun registration(user: UserDto) =
        araokRepository.registration(user)

    suspend fun accessToken(refreshJwtRequest: RefreshJwtRequestDto) =
        araokRepository.accessToken(refreshJwtRequest)

    suspend fun refreshToken(refreshJwtRequest: RefreshJwtRequestDto) =
        araokRepository.refreshToken(refreshJwtRequest)
}