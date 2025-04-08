package jpeg.compression.issue.app

import android.graphics.YuvImage
import androidx.annotation.IntRange
import jpeg.compression.issue.app.utils.TestImageWidth

class TestParameters(
    val image: YuvImage,
    val testImageWidth: TestImageWidth,
    @IntRange(0, 100)
    val jpegQuality: Int
) {

    override fun toString(): String {
        return "${TestParameters::class.simpleName}(" +
                "image=${imageToString()}, " +
                "testImageWidth=${testImageWidth}, " +
                "jpegQuality=$jpegQuality" +
                ")"
    }

    private fun imageToString(): String {
        return "${image::class.simpleName}(width=${image.width}, height=${image.height})"
    }
}