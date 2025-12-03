package com.jasz.ultrapdf.domain.usecases

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import org.apache.pdfbox.pdmodel.common.PDRectangle
import org.apache.pdfbox.pdmodel.PDPageContentStream
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject
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

            val imageXObject = PDImageXObject.createFromForm(newDocument, context.contentResolver.openInputStream(uri))
            val contentStream = PDPageContentStream(newDocument, page)
            contentStream.drawImage(imageXObject, 0f, 0f)
            contentStream.close()
        }

        newDocument.save(outputFile)
        newDocument.close()

        return outputFile.absolutePath
    }
}
