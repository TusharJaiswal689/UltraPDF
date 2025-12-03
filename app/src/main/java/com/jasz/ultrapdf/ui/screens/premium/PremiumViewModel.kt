package com.jasz.ultrapdf.ui.screens.premium

import android.app.Activity
import androidx.lifecycle.ViewModel
import com.android.billingclient.api.ProductDetails
import com.jasz.ultrapdf.billing.BillingManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class PremiumViewModel @Inject constructor(
    private val billingManager: BillingManager
) : ViewModel() {

    val products: StateFlow<List<ProductDetails>> = billingManager.products

    fun purchase(activity: Activity, productDetails: ProductDetails) {
        billingManager.launchBillingFlow(activity, productDetails)
    }

    fun restorePurchases() {
        billingManager.queryPurchases()
    }
}
