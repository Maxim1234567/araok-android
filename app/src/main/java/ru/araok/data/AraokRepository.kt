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
    suspend fun getAgeLimit(): List<AgeLimit> {
        return RetrofitService.araokApi.getAgeLimit().body() ?: emptyList()
    }

    suspend fun contentSave(content: ContentWithContentMediaAndMediaSubtitleDto): String {
        Log.d("AraokRepository", "contentSave")

        val stringJson = Moshi.Builder()
            .add(LocalDateAdapter())
            .addLast(KotlinJsonAdapterFactory())
            .build()
            .adapter(ContentWithContentMediaAndMediaSubtitleDto::class.java)
            .toJson(content)

        Log.d("AraokRepository", "contentSave: $stringJson")

        return RetrofitService.araokApi.contentSave(content).body() ?: "Not OK"
    }
}