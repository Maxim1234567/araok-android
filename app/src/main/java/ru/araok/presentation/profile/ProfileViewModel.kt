package ru.araok.presentation.profile

import androidx.lifecycle.ViewModel
import ru.araok.domain.GetAraokUseCase
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    getAraokUseCase: GetAraokUseCase
): ViewModel() {
}