package com.jasz.ultrapdf.ui.screens.imagecompression

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jasz.ultrapdf.domain.usecases.CompressImageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ImageCompressionViewModel @Inject constructor(
    private val compressImageUseCase: CompressImageUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<ImageCompressionState>(ImageCompressionState.Idle)
    val uiState: StateFlow<ImageCompressionState> = _uiState

    fun compressImage(imageUri: Uri, quality: Int) {
        viewModelScope.launch {
            _uiState.value = ImageCompressionState.Compressing
            try {
                val compressedPath = compressImageUseCase(imageUri, quality)
                _uiState.value = ImageCompressionState.Success(compressedPath)
            } catch (e: Exception) {
                _uiState.value = ImageCompressionState.Error(e.message ?: "An unknown error occurred")
            }
        }
    }
}

sealed class ImageCompressionState {
    object Idle : ImageCompressionState()
    object Compressing : ImageCompressionState()
    data class Success(val compressedImagePath: String) : ImageCompressionState()
    data class Error(val message: String) : ImageCompressionState()
}
