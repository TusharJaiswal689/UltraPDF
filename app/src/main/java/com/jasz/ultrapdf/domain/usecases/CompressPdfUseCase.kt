package com.jasz.ultrapdf.domain.usecases

import android.content.Context
import android.net.Uri
import com.tom_roush.pdfbox.pdmodel.PDDocument
import com.tom_roush.pdfbox.pdmodel.PDPage
import com.tom_roush.pdfbox.pdmodel.PDPageContentStream
import com.tom_roush.pdfbox.pdmodel.graphics.image.JPEGFactory
import com.tom_roush.pdfbox.util.Matrix
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject

class CompressPdfUseCase @Inject constructor(
    @ApplicationContext private val context: Context
) {
    suspend operator fun invoke(pdfUri: Uri, quality: Int): String {
        val inputStream = context.contentResolver.openInputStream(pdfUri)
        val document = PDDocument.load(inputStream)

        val outputDir = context.getExternalFilesDir("compressed_pdfs")
        if (outputDir != null && !outputDir.exists()) {
            outputDir.mkdirs()
        }

        val outputFile = File(outputDir, "compressed_${System.currentTimeMillis()}.pdf")

        val newDocument = PDDocument()
        for (page in document.pages) {
            val newPage = newDocument.importPage(page)
            val image = page.resources.images.firstOrNull()

            if (image != null) {
                val compressedImage = JPEGFactory.createFromImage(newDocument, image.image, quality / 100f)
                val contentStream = PDPageContentStream(newDocument, newPage, PDPageContentStream.AppendMode.APPEND, true, true)
                contentStream.drawImage(compressedImage, Matrix())
                contentStream.close()
            }
        }

        newDocument.save(outputFile)
        newDocument.close()
        document.close()

        return outputFile.absolutePath
    }
}
