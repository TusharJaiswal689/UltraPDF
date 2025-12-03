package com.jasz.ultrapdf.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jasz.ultrapdf.data.repository.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    val isPremium = userPreferencesRepository.isPremium
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val isOcrUnlocked = userPreferencesRepository.isOcrUnlocked
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)
}
