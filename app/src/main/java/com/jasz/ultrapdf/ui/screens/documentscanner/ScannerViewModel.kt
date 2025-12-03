package com.jasz.ultrapdf.ui.screens.documentscanner

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jasz.ultrapdf.domain.usecases.ScanDocumentUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScannerViewModel @Inject constructor(
    private val scanDocumentUseCase: ScanDocumentUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<ScannerState>(ScannerState.Idle)
    val uiState: StateFlow<ScannerState> = _uiState

    fun scanDocument(imageUri: Uri) {
        viewModelScope.launch {
            _uiState.value = ScannerState.Scanning
            try {
                val scannedImagePath = scanDocumentUseCase(imageUri)
                _uiState.value = ScannerState.Success(scannedImagePath)
            } catch (e: Exception) {
                _uiState.value = ScannerState.Error(e.message ?: "An unknown error occurred")
            }
        }
    }
}

sealed class ScannerState {
    object Idle : ScannerState()
    object Scanning : ScannerState()
    data class Success(val scannedImagePath: String) : ScannerState()
    data class Error(val message: String) : ScannerState()
}
