package ru.araok.data

import retrofit2.Retrofit
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Response
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import ru.araok.data.dto.AgeLimitDto
import ru.araok.data.dto.ContentWithContentMediaAndMediaSubtitleDto
import ru.araok.data.dto.LocalDateAdapter

private const val BASE_URL = "http://10.0.2.2:8080"

object RetrofitService {
    private val adapter = Moshi.Builder()
        .add(LocalDateAdapter())
        .add(KotlinJsonAdapterFactory())
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(
            MoshiConverterFactory.create(adapter)
        ).build()

    val araokApi: AraokApi = retrofit.create(
        AraokApi::class.java
    )

    interface AraokApi {
        @GET("/api/limit")
        suspend fun getAgeLimit(): Response<List<AgeLimitDto>>

        @POST("/api/content")
        suspend fun contentSave(@Body content: ContentWithContentMediaAndMediaSubtitleDto): Response<String>
    }
}