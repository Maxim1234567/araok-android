package ru.araok.data

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.squareup.moshi.Moshi
import com.squareup.moshi.addAdapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Response
import ru.araok.consts.TypeContent
import ru.araok.data.dto.*
import ru.araok.entites.AgeLimit
import ru.araok.entites.Content
import ru.araok.entites.Language
import ru.araok.entites.MediaSubtitle
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

    //media subtitle
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getSubtitle(contentId: Long, languageId: Long): MediaSubtitle =
        RetrofitService.araokApi.getSubtitle(contentId, languageId).body() ?: MediaSubtitleDto()
}