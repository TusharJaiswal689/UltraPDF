package com.jasz.ultrapdf.ui.screens.result

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.jasz.ultrapdf.ui.navigation.Screen
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultScreen(navController: NavController, filePath: String) {
    val context = LocalContext.current
    val file = File(filePath)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Result") },
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
            Text("Operation successful!")
            Spacer(modifier = Modifier.height(8.dp))
            Text("File saved to: $filePath")
            Spacer(modifier = Modifier.height(16.dp))

            if (filePath.endsWith(".jpg", ignoreCase = true)) {
                Image(
                    painter = rememberAsyncImagePainter(file),
                    contentDescription = "Processed Image",
                    modifier = Modifier.weight(1f)
                )
            } else {
                // Placeholder for other file types, like PDF
                Text("File type: ${file.extension}", modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
                val mimeType = if (filePath.endsWith(".jpg", ignoreCase = true)) "image/jpeg" else "application/pdf"

                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = mimeType
                    putExtra(Intent.EXTRA_STREAM, uri)
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }
                context.startActivity(Intent.createChooser(intent, "Share File"))
            }) {
                Text("Share")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = { navController.navigate(Screen.Home.route) { popUpTo(Screen.Home.route) { inclusive = true } } }) {
                Text("Done")
            }
        }
    }
}
