package jpeg.compression.issue.app.utils

import android.graphics.ImageFormat
import android.graphics.YuvImage

object YuvImageGenerator {

    fun createNv21YuvImage(
        width: TestImageWidth,
        height: Int,
    ): YuvImage {
        // NV21 format: Y plane size is width*height and the interleaved VU plane is width*height/2.
        val imageWidth = width.overallWidth
        val frameSize = imageWidth * height
        val nv21Size = frameSize + frameSize / 2
        val data = ByteArray(nv21Size)

        // Fill the Y (luma) plane with white and black columns
        for (y in 0 until height) {
            for (x in 0 until imageWidth) {
                val pixelIndex = y * imageWidth + x
                // Check if this pixel is in the white section or black section
                data[pixelIndex] = if (x < width.whiteComponentWidth) {
                    255.toByte() // White pixel
                } else {
                    0.toByte()   // Black pixel
                }
            }
        }

        // Fill the VU (chroma) plane.
        // In NV21, chroma data is interleaved with V followed by U for each 2x2 block.
        // The chroma values are neutral (128)
        val uValue = 128
        val vValue = 128
        var uvIndex = frameSize
        for (j in 0 until height / 2) {
            for (i in 0 until imageWidth / 2) {
                data[uvIndex++] = vValue.toByte()
                data[uvIndex++] = uValue.toByte()
            }
        }

        // Create and return the YuvImage.
        return YuvImage(data, ImageFormat.NV21, imageWidth, height, null)
    }
}