package com.jasz.ultrapdf.ui.screens.pdfcompression

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jasz.ultrapdf.domain.usecases.CompressPdfUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PdfCompressionViewModel @Inject constructor(
    private val compressPdfUseCase: CompressPdfUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<PdfCompressionState>(PdfCompressionState.Idle)
    val uiState: StateFlow<PdfCompressionState> = _uiState

    fun compressPdf(pdfUri: Uri, quality: Int) {
        viewModelScope.launch {
            _uiState.value = PdfCompressionState.Compressing
            try {
                val compressedPath = compressPdfUseCase(pdfUri, quality)
                _uiState.value = PdfCompressionState.Success(compressedPath)
            } catch (e: Exception) {
                _uiState.value = PdfCompressionState.Error(e.message ?: "An unknown error occurred")
            }
        }
    }
}

sealed class PdfCompressionState {
    object Idle : PdfCompressionState()
    object Compressing : PdfCompressionState()
    data class Success(val compressedPdfPath: String) : PdfCompressionState()
    data class Error(val message: String) : PdfCompressionState()
}
