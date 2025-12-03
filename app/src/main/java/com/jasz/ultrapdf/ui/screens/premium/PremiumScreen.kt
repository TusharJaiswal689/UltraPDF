package com.jasz.ultrapdf.ui.screens.premium

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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.jasz.ultrapdf.billing.Products
import com.jasz.ultrapdf.ui.screens.documentscanner.findActivity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PremiumScreen(
    navController: NavController,
    viewModel: PremiumViewModel = hiltViewModel()
) {
    val products by viewModel.products.collectAsStateWithLifecycle()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Go Premium") },
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
            products.find { it.productId == Products.REMOVE_ADS }?.let {
                PurchaseOptionCard(
                    title = it.name,
                    description = it.description,
                    onPurchase = { viewModel.purchase(context.findActivity(), it) }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            products.find { it.productId == Products.UNLOCK_OCR }?.let {
                PurchaseOptionCard(
                    title = it.name,
                    description = it.description,
                    onPurchase = { viewModel.purchase(context.findActivity(), it) }
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(onClick = { viewModel.restorePurchases() }) {
                Text("Restore Purchases")
            }
        }
    }
}

@Composable
fun PurchaseOptionCard(title: String, description: String, onPurchase: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = title, style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = description, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onPurchase, modifier = Modifier.align(Alignment.End)) {
                Text("Purchase")
            }
        }
    }
}
