package com.jasz.ultrapdf.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.jasz.ultrapdf.ui.screens.documentscanner.ScannerScreen
import com.jasz.ultrapdf.ui.screens.home.HomeScreen
import com.jasz.ultrapdf.ui.screens.imagecompression.ImageCompressionScreen
import com.jasz.ultrapdf.ui.screens.imagetopdf.ImageToPdfScreen
import com.jasz.ultrapdf.ui.screens.myfiles.MyFilesScreen
import com.jasz.ultrapdf.ui.screens.ocr.OCRScreen
import com.jasz.ultrapdf.ui.screens.pdfcompression.PdfCompressionScreen
import com.jasz.ultrapdf.ui.screens.premium.PremiumScreen
import com.jasz.ultrapdf.ui.screens.result.ResultScreen
import com.jasz.ultrapdf.ui.screens.settings.SettingsScreen
import com.jasz.ultrapdf.ui.screens.splash.SplashScreen

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Splash.route) {
        composable(Screen.Splash.route) {
            SplashScreen(navController)
        }
        composable(Screen.Home.route) {
            HomeScreen(navController)
        }
        composable(Screen.ImageCompress.route) {
            ImageCompressionScreen(navController)
        }
        composable(Screen.PdfCompress.route) {
            PdfCompressionScreen(navController)
        }
        composable(Screen.ImageToPdf.route) {
            ImageToPdfScreen(navController)
        }
        composable(Screen.DocScanner.route) {
            ScannerScreen(navController)
        }
        composable(Screen.OcrReader.route) {
            OCRScreen(navController)
        }
        composable(
            route = Screen.Result.route,
            arguments = listOf(navArgument("filePath") { type = NavType.StringType })
        ) { backStackEntry ->
            val filePath = backStackEntry.arguments?.getString("filePath") ?: ""
            ResultScreen(navController, filePath)
        }
        composable(Screen.Settings.route) {
            SettingsScreen(navController)
        }
        composable(Screen.Premium.route) {
            PremiumScreen(navController)
        }
        composable(Screen.MyFiles.route) {
            MyFilesScreen(navController)
        }
    }
}
