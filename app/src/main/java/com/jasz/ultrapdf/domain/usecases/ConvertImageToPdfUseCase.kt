package com.jasz.ultrapdf.domain.usecases

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.tom_roush.pdfbox.pdmodel.PDDocument
import com.tom_roush.pdfbox.pdmodel.PDPage
import com.tom_roush.pdfbox.pdmodel.common.PDRectangle
import com.tom_roush.pdfbox.pdmodel.PDPageContentStream
import com.tom_roush.pdfbox.pdmodel.graphics.image.PDImageXObject
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.ByteArrayOutputStream
import java.io.File
import javax.inject.Inject

class ConvertImageToPdfUseCase @Inject constructor(
    @ApplicationContext private val context: Context
) {
    suspend operator fun invoke(imageUris: List<Uri>): String {
        val newDocument = PDDocument()
        val outputDir = context.getExternalFilesDir("converted_pdfs")
        if (outputDir != null && !outputDir.exists()) {
            outputDir.mkdirs()
        }

        val outputFile = File(outputDir, "converted_${System.currentTimeMillis()}.pdf")

        for (uri in imageUris) {
            val inputStream = context.contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            val page = PDPage(PDRectangle(bitmap.width.toFloat(), bitmap.height.toFloat()))
            newDocument.addPage(page)

            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val imageBytes = baos.toByteArray()

            val imageXObject = PDImageXObject.createFromByteArray(newDocument, imageBytes, "image")
            val contentStream = PDPageContentStream(newDocument, page)
            contentStream.drawImage(imageXObject, 0f, 0f)
            contentStream.close()
        }

        newDocument.save(outputFile)
        newDocument.close()

        return outputFile.absolutePath
    }
}
