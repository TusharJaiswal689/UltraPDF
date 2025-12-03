package com.jasz.ultrapdf.ui.screens.documentscanner

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ScannerViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow<ScannerState>(ScannerState.Idle)
    val uiState: StateFlow<ScannerState> = _uiState

    fun onScanResult(pdfPath: String) {
        _uiState.value = ScannerState.Success(pdfPath)
    }

    fun onError(errorMessage: String) {
        _uiState.value = ScannerState.Error(errorMessage)
    }
}

sealed class ScannerState {
    object Idle : ScannerState()
    data class Success(val pdfPath: String) : ScannerState()
    data class Error(val message: String) : ScannerState()
}
