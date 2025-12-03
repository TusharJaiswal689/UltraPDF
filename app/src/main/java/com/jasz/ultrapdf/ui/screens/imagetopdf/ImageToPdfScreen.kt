package com.jasz.ultrapdf.ui.screens.imagetopdf

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.jasz.ultrapdf.ui.navigation.Screen
import java.net.URLEncoder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageToPdfScreen(
    navController: NavController,
    viewModel: ImageToPdfViewModel = hiltViewModel()
) {
    var imageUris by remember { mutableStateOf<List<Uri>>(emptyList()) }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents(),
        onResult = { uris ->
            imageUris = uris
        }
    )

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Image to PDF") },
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
            Button(onClick = { imagePicker.launch("image/*") }) {
                Text("Select Images")
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (imageUris.isNotEmpty()) {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(imageUris) { uri ->
                        Image(
                            painter = rememberAsyncImagePainter(uri),
                            contentDescription = "Selected Image",
                            modifier = Modifier.size(100.dp),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { viewModel.convertImagesToPdf(imageUris) },
                    enabled = uiState != ImageToPdfState.Converting
                ) {
                    Text("Convert to PDF")
                }
            }

            when (val state = uiState) {
                is ImageToPdfState.Converting -> {
                    Spacer(modifier = Modifier.height(16.dp))
                    CircularProgressIndicator()
                }
                is ImageToPdfState.Success -> {
                    LaunchedEffect(state) {
                        val encodedPath = URLEncoder.encode(state.pdfPath, "UTF-8")
                        navController.navigate(Screen.Result.createRoute(encodedPath))
                    }
                }
                is ImageToPdfState.Error -> {
                    LaunchedEffect(snackbarHostState) {
                        snackbarHostState.showSnackbar(message = state.message)
                    }
                }
                else -> {}
            }
        }
    }
}
