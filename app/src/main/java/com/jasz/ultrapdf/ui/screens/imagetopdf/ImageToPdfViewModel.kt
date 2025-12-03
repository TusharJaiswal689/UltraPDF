package com.jasz.ultrapdf.ui.screens.imagetopdf

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jasz.ultrapdf.domain.usecases.ConvertImageToPdfUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ImageToPdfViewModel @Inject constructor(
    private val convertImageToPdfUseCase: ConvertImageToPdfUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<ImageToPdfState>(ImageToPdfState.Idle)
    val uiState: StateFlow<ImageToPdfState> = _uiState

    fun convertImagesToPdf(imageUris: List<Uri>) {
        viewModelScope.launch {
            _uiState.value = ImageToPdfState.Converting
            try {
                val pdfPath = convertImageToPdfUseCase(imageUris)
                _uiState.value = ImageToPdfState.Success(pdfPath)
            } catch (e: Exception) {
                _uiState.value = ImageToPdfState.Error(e.message ?: "An unknown error occurred")
            }
        }
    }
}

sealed class ImageToPdfState {
    object Idle : ImageToPdfState()
    object Converting : ImageToPdfState()
    data class Success(val pdfPath: String) : ImageToPdfState()
    data class Error(val message: String) : ImageToPdfState()
}
