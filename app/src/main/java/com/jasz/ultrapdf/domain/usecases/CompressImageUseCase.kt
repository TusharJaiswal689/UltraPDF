package com.jasz.ultrapdf.domain.usecases

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import dagger.hilt.android.qualifiers.ApplicationContext
import id.zelory.compressor.Compressor
import java.io.File
import javax.inject.Inject

class CompressImageUseCase @Inject constructor(
    @ApplicationContext private val context: Context
) {
    suspend operator fun invoke(imageUri: Uri, quality: Int): String {
        val imageFile = imageUri.toFile(context)
        val compressedImageFile = Compressor.compress(context, imageFile) {
            quality(quality)
        }
        return compressedImageFile.absolutePath
    }
}

private fun Uri.toFile(context: Context): File {
    val projection = arrayOf(MediaStore.Images.Media.DATA)
    val cursor = context.contentResolver.query(this, projection, null, null, null)
    val columnIndex = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
    cursor?.moveToFirst()
    val path = cursor?.getString(columnIndex ?: 0) ?: ""
    cursor?.close()
    return File(path)
}
