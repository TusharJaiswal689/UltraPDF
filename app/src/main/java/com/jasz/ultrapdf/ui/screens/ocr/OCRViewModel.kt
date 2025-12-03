package com.jasz.ultrapdf.ui.screens.ocr

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jasz.ultrapdf.domain.usecases.PerformOcrUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OCRViewModel @Inject constructor(
    private val performOcrUseCase: PerformOcrUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<OcrState>(OcrState.Idle)
    val uiState: StateFlow<OcrState> = _uiState

    fun performOcr(imageUri: Uri) {
        viewModelScope.launch {
            _uiState.value = OcrState.Processing
            try {
                val extractedText = performOcrUseCase(imageUri)
                _uiState.value = OcrState.Success(extractedText)
            } catch (e: Exception) {
                _uiState.value = OcrState.Error(e.message ?: "An unknown error occurred")
            }
        }
    }
}

sealed class OcrState {
    object Idle : OcrState()
    object Processing : OcrState()
    data class Success(val extractedText: String) : OcrState()
    data class Error(val message: String) : OcrState()
}
