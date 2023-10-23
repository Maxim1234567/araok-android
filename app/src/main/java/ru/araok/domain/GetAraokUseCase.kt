package ru.araok.domain

import android.util.Log
import ru.araok.data.AraokRepository
import ru.araok.data.dto.ContentWithContentMediaAndMediaSubtitleDto
import ru.araok.entites.AgeLimit
import javax.inject.Inject

class GetAraokUseCase @Inject constructor(
    private val araokRepository: AraokRepository
) {
    suspend fun getAgeLimit(): List<AgeLimit> {
        return araokRepository.getAgeLimit()
    }

    suspend fun contentSave(content: ContentWithContentMediaAndMediaSubtitleDto): String {
        Log.d("GetAraokUseCase", "uploadMedia")

        return araokRepository.contentSave(content);
    }
}