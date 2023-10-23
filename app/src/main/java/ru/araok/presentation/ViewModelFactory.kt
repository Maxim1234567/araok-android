package ru.araok.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.araok.presentation.videopage.VideoPageViewModel
import javax.inject.Inject

class ViewModelFactory @Inject constructor(
    private val videoPageViewModel: VideoPageViewModel
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(VideoPageViewModel::class.java)) {
            return videoPageViewModel as T
        }

        throw IllegalArgumentException("Unknown class name")
    }
}