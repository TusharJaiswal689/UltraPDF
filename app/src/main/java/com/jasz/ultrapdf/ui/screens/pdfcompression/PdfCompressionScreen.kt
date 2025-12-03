package com.jasz.ultrapdf.ui.screens.pdfcompression

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.jasz.ultrapdf.ui.navigation.Screen
import java.net.URLEncoder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PdfCompressionScreen(
    navController: NavController,
    viewModel: PdfCompressionViewModel = hiltViewModel()
) {
    var pdfUri by remember { mutableStateOf<Uri?>(null) }
    var compressionLevel by remember { mutableStateOf(50f) }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val pdfPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            pdfUri = uri
        }
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Compress PDF") },
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
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            pdfUri?.let {
                Text("Selected PDF: ${it.path}")
                Spacer(modifier = Modifier.height(16.dp))
            }

            Button(onClick = { pdfPicker.launch("application/pdf") }) {
                Text("Select PDF")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Compression Level: ${compressionLevel.toInt()}%")
            Slider(
                value = compressionLevel,
                onValueChange = { compressionLevel = it },
                valueRange = 0f..100f,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    pdfUri?.let { uri ->
                        viewModel.compressPdf(uri, compressionLevel.toInt())
                    }
                },
                enabled = pdfUri != null && uiState != PdfCompressionState.Compressing
            ) {
                Text("Compress PDF")
            }

            when (val state = uiState) {
                is PdfCompressionState.Compressing -> {
                    Spacer(modifier = Modifier.height(16.dp))
                    CircularProgressIndicator()
                }
                is PdfCompressionState.Success -> {
                    LaunchedEffect(state) {
                        val encodedPath = URLEncoder.encode(state.compressedPdfPath, "UTF-8")
                        navController.navigate(Screen.Result.createRoute(encodedPath))
                    }
                }
                is PdfCompressionState.Error -> {
                    // TODO: Show error message
                }
                else -> {}
            }
        }
    }
}
