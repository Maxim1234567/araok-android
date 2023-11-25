package ru.araok.data

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import ru.araok.consts.TypeContent
import ru.araok.data.dto.*
import ru.araok.entites.*
import javax.inject.Inject

class AraokRepository @Inject constructor() {
    //age limit
    suspend fun getAgeLimit(): List<AgeLimit> =
        RetrofitService.araokApi.getAgeLimit().body() ?: emptyList()

    //content
    suspend fun getContents(type: TypeContent): List<Content> =
        RetrofitService.araokApi.getContents(type.name).body() ?: emptyList()

    suspend fun getContentsByName(name: String): List<Content> =
        RetrofitService.araokApi.getContentsByName(name).body() ?: emptyList()

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getContentById(id: Long): Content =
        RetrofitService.araokApi.getContentById(id).body() ?: ContentDto()

    suspend fun contentSave(content: ContentWithContentMediaAndMediaSubtitleDto): String =
        RetrofitService.araokApi.contentSave(content).body() ?: "Not OK"

    //language
    suspend fun getAllLanguages(): List<Language> =
        RetrofitService.araokApi.getAllLanguages().body() ?: emptyList()

    suspend fun getAllLanguageSubtitle(contentId: Long) =
        RetrofitService.araokApi.getAllLanguageSubtitle(contentId).body() ?: emptyList()

    //media subtitle
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getSubtitle(contentId: Long, languageId: Long) =
        RetrofitService.araokApi.getSubtitle(contentId, languageId).body() ?: MediaSubtitleDto()

    //media
    suspend fun getMedia(contentId: Long, typeId: Long): List<Byte> {
        Log.d("AraokRepository", "contentId: $contentId, typeId: $typeId");

        val response = RetrofitService.araokApi.getMedia(contentId, typeId)
        return response.bytes().asList()
    }

    //setting
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getSetting(contentId: Long) =
        RetrofitService.araokApi.getSetting(contentId).body() ?: SettingsDto()

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun saveSetting(settings: SettingsDto) =
        RetrofitService.araokApi.settingSave(settings).body() ?: SettingsDto()

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun updateSetting(settings: SettingsDto) =
        RetrofitService.araokApi.settingUpdate(settings).body() ?: SettingsDto()

    //authorization
    suspend fun login(jwtRequest: JwtRequestDto) =
        RetrofitService.araokApi.login(jwtRequest).body() ?: JwtRequestDto()

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun registration(user: UserDto) =
        RetrofitService.araokApi.registration(user).body() ?: UserDto()

    suspend fun accessToken(refreshJwtRequest: RefreshJwtRequestDto) =
        RetrofitService.araokApi.accessToken(refreshJwtRequest)

    suspend fun refreshToken(refreshJwtRequest: RefreshJwtRequestDto) =
        RetrofitService.araokApi.refreshToken(refreshJwtRequest)
}