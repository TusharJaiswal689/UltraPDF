package com.jasz.ultrapdf.ui.screens.ocr

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OCRScreen(
    navController: NavController,
    viewModel: OCRViewModel = hiltViewModel()
) {
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var extractedText by remember { mutableStateOf("") }

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            imageUri = uri
        }
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("OCR Reader") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (imageUri != null) {
                Image(
                    painter = rememberAsyncImagePainter(imageUri),
                    contentDescription = "Selected Image for OCR",
                    modifier = Modifier.height(200.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            Button(onClick = { imagePicker.launch("image/*") }) {
                Text("Select Image")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { imageUri?.let { viewModel.performOcr(it) } },
                enabled = imageUri != null && uiState != OcrState.Processing
            ) {
                Text("Extract Text")
            }

            when (val state = uiState) {
                is OcrState.Processing -> {
                    Spacer(modifier = Modifier.height(16.dp))
                    CircularProgressIndicator()
                }
                is OcrState.Success -> {
                    extractedText = state.extractedText
                }
                is OcrState.Error -> {
                    // TODO: Show error message
                }
                else -> {}
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = extractedText,
                onValueChange = { extractedText = it },
                label = { Text("Extracted Text") },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
        }
    }
}
