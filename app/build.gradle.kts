plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.compose)
    alias(libs.plugins.hilt)
}

android {
    namespace = "com.yourpackage.app"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.yourpackage.app"
        minSdk = 31
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        vectorDrawables.useSupportLibrary = true
    }

    buildFeatures {
        compose = true
    }

    packaging.resources {
        excludes += "/META-INF/{LICENSE,LICENSE.txt,NOTICE,NOTICE.txt}"
    }
}

dependencies {
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.compose.foundation)
    implementation(libs.compose.material3)
    implementation(libs.compose.preview)
    debugImplementation(libs.compose.ui.tooling)

    implementation(libs.core.ktx)
    implementation(libs.lifecycle.runtime)
    implementation(libs.lifecycle.runtime.compose)
    implementation(libs.viewmodel.compose)

    implementation(libs.activity.compose)
    implementation(libs.navigation.compose)

    implementation(libs.accompanist.permissions)

    implementation(libs.coroutines.android)
    implementation(libs.datastore.preferences)

    implementation(libs.coil.compose)

    implementation(libs.camerax.core)
    implementation(libs.camerax.camera2)
    implementation(libs.camerax.lifecycle)
    implementation(libs.camerax.view)

    implementation(libs.mlkit.ocr)
    implementation(libs.mlkit.document.scanner)
    implementation(libs.billing)
    implementation(libs.google.ads)

    implementation(libs.pdfbox)
    implementation(libs.pdfbox.fontbox)
    implementation(libs.pdfbox.tools)
    coreLibraryDesugaring(libs.desugar)
//    implementation(libs.opencv)

    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

}
