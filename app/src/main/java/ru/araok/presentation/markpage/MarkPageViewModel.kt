package ru.araok.presentation.markpage

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.araok.data.db.SettingsMarksDb
import ru.araok.data.db.SettingsWithMarksDb
import ru.araok.domain.GetAraokDbUseCase
import javax.inject.Inject

class MarkPageViewModel @Inject constructor(
    private val getAraokDbUseCase: GetAraokDbUseCase
): ViewModel() {
    var updateView = true

    private val _load = MutableStateFlow(State.EMPTY)

    val load: StateFlow<State> = _load.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = _load.value
    )

    lateinit var settings: StateFlow<SettingsWithMarksDb>

    fun loadMarks(contentId: Int) {
        updateView = true
        settings = getAraokDbUseCase.getSettingsWithMarks(contentId)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = SettingsWithMarksDb()
            )
    }

    fun addSettingsWithMarks(settingsWithMarksDb: SettingsWithMarksDb) {
        updateView = false
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                _load.value = State.PROCESS
                getAraokDbUseCase.deleteSettings(settingsWithMarksDb.settingDb.contentId!!)
                getAraokDbUseCase.insertSettingWithMarks(settingsWithMarksDb)
            }.fold(
                onSuccess = { Log.d("MarkPageViewModel", "onSuccess"); _load.value = State.LOAD; },
                onFailure = { Log.d("MarkPageViewModel", it.message ?: "Error") }
            )
        }
    }
}