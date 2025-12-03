package com.jasz.ultrapdf.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.jasz.ultrapdf.ui.MainViewModel
import com.jasz.ultrapdf.ui.ads.BannerAdView
import com.jasz.ultrapdf.ui.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    mainViewModel: MainViewModel = hiltViewModel()
) {
    val isPremium by mainViewModel.isPremium.collectAsStateWithLifecycle()
    val isOcrUnlocked by mainViewModel.isOcrUnlocked.collectAsStateWithLifecycle()
    var showMenu by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("UltraPDF") },
                actions = {
                    IconButton(onClick = { navController.navigate(Screen.Settings.route) }) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                    IconButton(onClick = { showMenu = !showMenu }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "More")
                    }
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Go Premium") },
                            onClick = {
                                navController.navigate(Screen.Premium.route)
                                showMenu = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("My Files") },
                            onClick = {
                                navController.navigate(Screen.MyFiles.route)
                                showMenu = false
                            }
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            val features = listOf(
                "Compress Image" to Screen.ImageCompress.route,
                "Compress PDF" to Screen.PdfCompress.route,
                "Image to PDF" to Screen.ImageToPdf.route,
                "Scan Document" to Screen.DocScanner.route,
                "OCR Reader" to Screen.OcrReader.route
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(features) { (name, route) ->
                    Button(
                        onClick = { navController.navigate(route) },
                        enabled = if (route == Screen.OcrReader.route) isOcrUnlocked else true
                    ) {
                        Text(name)
                    }
                }
            }

            if (!isPremium) {
                BannerAdView(
                    adUnitId = "ca-app-pub-3940256099942544/6300978111", // Test ad unit ID
                    modifier = Modifier.align(Alignment.BottomCenter)
                )
            }
        }
    }
}
