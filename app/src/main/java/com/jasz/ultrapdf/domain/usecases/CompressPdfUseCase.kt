package com.jasz.ultrapdf.domain.usecases

import android.content.Context
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import org.apache.pdfbox.pdmodel.PDResources
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject
import java.awt.image.BufferedImage
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

        for (page in document.pages) {
            val resources: PDResources = page.resources
            for (name in resources.xObjectNames) {
                val xObject = resources.getXObject(name)
                if (xObject is PDImageXObject) {
                    val image: BufferedImage = xObject.image
                    val compressedImage = LosslessFactory.createFromImage(document, image)
                    resources.put(name, compressedImage)
                }
            }
        }

        document.save(outputFile)
        document.close()

        return outputFile.absolutePath
    }
}
