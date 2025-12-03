package com.jasz.ultrapdf.ui.screens.documentscanner

import android.Manifest
import android.net.Uri
import androidx.camera.core.ImageCapture
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.jasz.ultrapdf.ui.navigation.Screen
import kotlinx.coroutines.launch
import java.net.URLEncoder

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ScannerScreen(
    navController: NavController,
    viewModel: ScannerViewModel = hiltViewModel()
) {
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)
    val imageCapture = remember { ImageCapture.Builder().build() }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Scan Document") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            if (cameraPermissionState.status.isGranted) {
                ScannerCameraView(
                    imageCapture = imageCapture,
                    onImageCaptured = { result ->
                        result.savedUri?.let { viewModel.scanDocument(it) }
                    },
                    onError = { /* TODO: Handle error */ }
                )
                IconButton(
                    onClick = {
                        coroutineScope.launch {
                            val result = imageCapture.takePicture(context)
                            result.savedUri?.let { viewModel.scanDocument(it) }
                        }
                    },
                    modifier = Modifier.align(Alignment.BottomCenter),
                    enabled = uiState != ScannerState.Scanning
                ) {
                    Icon(Icons.Default.Camera, contentDescription = "Capture")
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Camera permission is required to use this feature.")
                    Button(onClick = { cameraPermissionState.launchPermissionRequest() }) {
                        Text("Request Permission")
                    }
                }
            }

            when (val state = uiState) {
                is ScannerState.Scanning -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is ScannerState.Success -> {
                    LaunchedEffect(state) {
                        val encodedPath = URLEncoder.encode(state.scannedImagePath, "UTF-8")
                        navController.navigate(Screen.Result.createRoute(encodedPath))
                    }
                }
                is ScannerState.Error -> {
                    // TODO: Show error message
                }
                else -> {}
            }
        }
    }
}
