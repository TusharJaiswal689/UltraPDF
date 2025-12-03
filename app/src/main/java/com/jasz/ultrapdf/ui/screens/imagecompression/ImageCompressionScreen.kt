package com.jasz.ultrapdf.ui.screens.imagecompression

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
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
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
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
import coil.compose.rememberAsyncImagePainter
import com.jasz.ultrapdf.ui.navigation.Screen
import java.net.URLEncoder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageCompressionScreen(
    navController: NavController,
    viewModel: ImageCompressionViewModel = hiltViewModel()
) {
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var compressionQuality by remember { mutableStateOf(80f) }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            imageUri = uri
        }
    )

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Compress Image") },
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
            if (imageUri != null) {
                Image(
                    painter = rememberAsyncImagePainter(imageUri),
                    contentDescription = "Selected Image",
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            Button(onClick = { imagePicker.launch("image/*") }) {
                Text("Select Image")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Compression Quality: ${compressionQuality.toInt()}%")
            Slider(
                value = compressionQuality,
                onValueChange = { compressionQuality = it },
                valueRange = 0f..100f,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    imageUri?.let { uri ->
                        viewModel.compressImage(uri, compressionQuality.toInt())
                    }
                },
                enabled = imageUri != null && uiState != ImageCompressionState.Compressing
            ) {
                Text("Compress Image")
            }

            when (val state = uiState) {
                is ImageCompressionState.Compressing -> {
                    Spacer(modifier = Modifier.height(16.dp))
                    CircularProgressIndicator()
                }
                is ImageCompressionState.Success -> {
                    LaunchedEffect(state) {
                        val encodedPath = URLEncoder.encode(state.compressedImagePath, "UTF-8")
                        navController.navigate(Screen.Result.createRoute(encodedPath))
                    }
                }
                is ImageCompressionState.Error -> {
                    LaunchedEffect(snackbarHostState) {
                        snackbarHostState.showSnackbar(message = state.message)
                    }
                }
                else -> {}
            }
        }
    }
}
