package ru.araok.data

import android.util.Log
import com.squareup.moshi.Moshi
import com.squareup.moshi.addAdapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import ru.araok.data.dto.ContentWithContentMediaAndMediaSubtitleDto
import ru.araok.data.dto.LocalDateAdapter
import ru.araok.entites.AgeLimit
import javax.inject.Inject

class AraokRepository @Inject constructor() {
    suspend fun getAgeLimit(): List<AgeLimit> =
        RetrofitService.araokApi.getAgeLimit().body() ?: emptyList()

    suspend fun contentSave(content: ContentWithContentMediaAndMediaSubtitleDto): String =
        RetrofitService.araokApi.contentSave(content).body() ?: "Not OK"
}