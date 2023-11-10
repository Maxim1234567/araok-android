package ru.araok.presentation.videopage

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.araok.domain.GetAraokUseCase
import javax.inject.Inject

class VideoPageViewModel @Inject constructor(
    private val getAraokUseCase: GetAraokUseCase
): ViewModel() {
    private val _video = MutableStateFlow(ByteArray(0))
    val video: StateFlow<ByteArray> = _video.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = _video.value
    )

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
}