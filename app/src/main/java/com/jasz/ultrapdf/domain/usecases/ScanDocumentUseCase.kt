package com.jasz.ultrapdf.domain.usecases

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import com.quickbird.snapshot.toBitmap
import dagger.hilt.android.qualifiers.ApplicationContext
import org.opencv.android.Utils
import org.opencv.core.Mat
import org.opencv.core.MatOfPoint
import org.opencv.core.MatOfPoint2f
import org.opencv.core.Size
import org.opencv.imgproc.Imgproc
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

class ScanDocumentUseCase @Inject constructor(
    @ApplicationContext private val context: Context
) {
    suspend operator fun invoke(imageUri: Uri): String {
        // 1. Load the image and convert to a Mat object
        val imageBitmap = imageUri.toBitmap(context)
        val imageMat = Mat()
        Utils.bitmapToMat(imageBitmap, imageMat)

        // 2. Grayscale, blur, and edge detection
        val grayMat = Mat()
        Imgproc.cvtColor(imageMat, grayMat, Imgproc.COLOR_BGR2GRAY)
        val blurredMat = Mat()
        Imgproc.GaussianBlur(grayMat, blurredMat, Size(5.0, 5.0), 0.0)
        val edgedMat = Mat()
        Imgproc.Canny(blurredMat, edgedMat, 75.0, 200.0)

        // 3. Find contours
        val contours = mutableListOf<MatOfPoint>()
        val hierarchy = Mat()
        Imgproc.findContours(edgedMat, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE)

        // 4. Find the largest contour (the document)
        var maxArea = 0.0
        var largestContour: MatOfPoint? = null
        for (contour in contours) {
            val area = Imgproc.contourArea(contour)
            if (area > maxArea) {
                maxArea = area
                largestContour = contour
            }
        }

        // 5. Apply perspective transform
        val transformedBitmap = if (largestContour != null) {
            val points = largestContour.toArray()
            val corners = MatOfPoint2f(*points)
            val warpedMat = Mat()
            val size = Size(imageMat.width().toDouble(), imageMat.height().toDouble())
            val target = MatOfPoint2f(
                org.opencv.core.Point(0.0, 0.0),
                org.opencv.core.Point(size.width - 1, 0.0),
                org.opencv.core.Point(size.width - 1, size.height - 1),
                org.opencv.core.Point(0.0, size.height - 1)
            )

            val transform = Imgproc.getPerspectiveTransform(corners, target)
            Imgproc.warpPerspective(imageMat, warpedMat, transform, size)

            val resultBitmap = Bitmap.createBitmap(warpedMat.cols(), warpedMat.rows(), Bitmap.Config.ARGB_8888)
            Utils.matToBitmap(warpedMat, resultBitmap)
            resultBitmap
        } else {
            imageBitmap // Return original if no contour found
        }

        // 6. Save the resulting image
        val outputDir = context.getExternalFilesDir("scanned_documents")
        if (outputDir != null && !outputDir.exists()) {
            outputDir.mkdirs()
        }
        val outputFile = File(outputDir, "scanned_${System.currentTimeMillis()}.jpg")
        val outputStream = FileOutputStream(outputFile)
        transformedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        outputStream.close()

        return outputFile.absolutePath
    }
}
