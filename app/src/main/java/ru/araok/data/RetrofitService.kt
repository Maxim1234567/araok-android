package ru.araok.data

import retrofit2.Retrofit
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Response
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import ru.araok.data.dto.*

const val BASE_URL = "http://10.0.2.2:8765"

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
        //age-limit
        @GET("/api/limit")
        suspend fun getAgeLimit(): Response<List<AgeLimitDto>>

        //content
        @GET("/api/content")
        suspend fun getContents(@Query("type") type: String): Response<List<ContentDto>>

        @GET("/api/content/{name}")
        suspend fun getContentsByName(@Path("name") name: String): Response<List<ContentDto>>

        @GET("/api/content/id/{id}")
        suspend fun getContentById(@Path("id") id: Long): Response<ContentDto>

        @POST("/api/content")
        suspend fun contentSave(@Body content: ContentWithContentMediaAndMediaSubtitleDto): Response<String>

        //language
        @GET("/api/language")
        suspend fun getAllLanguages(): Response<List<LanguageDto>>

        //media subtitle
        @GET("/api/subtitle/{contentId}/{languageId}")
        suspend fun getSubtitle(
            @Path("contentId") contentId: Long,
            @Path("languageId") languageId: Long
        ): Response<MediaSubtitleDto>

        //media
        @GET("/api/media")
        suspend fun getMedia(
            @Path("contentId") contentId: Long,
            @Path("typeId") typeId: Long
        ): Response<List<Byte>>
    }
}