package ru.araok.presentation.videopage

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.araok.data.db.SettingsDb
import ru.araok.data.db.SettingsWithMarksDb
import ru.araok.domain.GetAraokDbUseCase
import ru.araok.domain.GetAraokUseCase
import ru.araok.entites.Language
import ru.araok.entites.SettingsWithMarks
import ru.araok.entites.Subtitle
import javax.inject.Inject

class VideoPageViewModel @Inject constructor(
    private val getAraokUseCase: GetAraokUseCase,
    private val getAraokDbUseCase: GetAraokDbUseCase
): ViewModel() {
    private val _languageFlow = MutableStateFlow(-1)
    val languageFlow = _languageFlow.asStateFlow()

    val selectLanguage: Int
        get() = _languageFlow.value

    private val _video = MutableStateFlow(ByteArray(0))
    val video: StateFlow<ByteArray> = _video.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = _video.value
    )

    private val _language = MutableStateFlow<List<Language>>(emptyList())
    val language: StateFlow<List<Language>> = _language.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = _language.value
    )

    private val _subtitle = MutableStateFlow<List<Subtitle>>(emptyList())
    val subtitle: StateFlow<List<Subtitle>> = _subtitle.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = _subtitle.value
    )

    private var _settingsDb = MutableStateFlow(
        SettingsWithMarksDb(settingDb = SettingsDb(id = -1, contentId = -1))
    )
    val settingsDb: StateFlow<SettingsWithMarks> = _settingsDb.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = _settingsDb.value
    )

    @RequiresApi(Build.VERSION_CODES.O)
    fun loadSubtitle(contentId: Long, languageId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                getAraokUseCase.getSubtitle(contentId, languageId)
            }.fold(
                onSuccess = { _subtitle.value = it.subtitles },
                onFailure = { Log.d("VideoPageViewModel", it.message ?: "") }
            )
        }
    }

    fun loadVideo(contentId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                Log.d("VideoPageViewModel", "contentId: $contentId")

                getAraokUseCase.getMedia(contentId)
            }.fold(
                onSuccess = { _video.value = it.toByteArray() },
                onFailure = { Log.d("VideoPageViewModel", it.message ?: "") }
            )
        }
    }

    fun loadSettings(contentId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                getAraokDbUseCase.loadSettingsWithMarks(contentId)
            }.fold(
                onSuccess = { _settingsDb.value = it },
                onFailure = { Log.d("VideoPageViewModel", it.message ?: "")}
            )
        }
    }

    fun getAllLanguageSubtitle(contentId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                getAraokUseCase.getAllLanguageSubtitle(contentId)
            }.fold(
                onSuccess = { _language.value = it },
                onFailure = { Log.d("SubtitleDialogViewModel", it.message ?: "") }
            )
        }
    }

    fun sendLanguageId(languageId: Int) {
        viewModelScope.launch {
            if(_languageFlow.value != languageId)
                _languageFlow.value = languageId
        }
    }
}