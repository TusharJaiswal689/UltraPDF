package com.jasz.ultrapdf.ui.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash_screen")
    object Home : Screen("home_screen")
    object ImageCompress : Screen("image_compress_screen")
    object PdfCompress : Screen("pdf_compress_screen")
    object ImageToPdf : Screen("image_to_pdf_screen")
    object DocScanner : Screen("doc_scanner_screen")
    object OcrReader : Screen("ocr_reader_screen")
    object Result : Screen("result_screen")
    object Settings : Screen("settings_screen")
    object Premium : Screen("premium_screen")
    object MyFiles : Screen("my_files_screen")
}