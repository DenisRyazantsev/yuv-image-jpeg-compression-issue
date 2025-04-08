package jpeg.compression.issue.app

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Rect
import android.graphics.YuvImage
import android.util.Log
import androidx.annotation.IntRange
import androidx.test.core.app.ApplicationProvider
import jpeg.compression.issue.app.utils.FileUtils
import jpeg.compression.issue.app.utils.TestImageWidth
import jpeg.compression.issue.app.utils.YuvImageGenerator
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import java.io.ByteArrayOutputStream


@RunWith(Parameterized::class)
class YuvImageJpegCompressionTest {

    fun assertThatJpegCompressionWorkWithoutArtefacts(
        image: YuvImage,
        testImageWidth: TestImageWidth,
        @IntRange(0, 100)
        jpegQuality: Int
    ) {
        val compressionRegion = Rect(0, 0, image.width, image.height)
        val jpegOutputStream = ByteArrayOutputStream()
        image.compressToJpeg(
            compressionRegion,
            jpegQuality,
            jpegOutputStream
        )
        val jpegData = jpegOutputStream.toByteArray()
        jpegOutputStream.close()

        // Save image for debug purposes
        FileUtils.saveJpeg(
            context,
            jpegData,
            "image-" +
                    "(${testImageWidth.whiteComponentWidth}+${testImageWidth.blackComponentWidth})x" +
                    "${image.height}-" +
                    "$jpegQuality.jpeg"
        )

        val jpegBitmap = BitmapFactory.decodeByteArray(jpegData, 0, jpegData.size)

        val edgeColor = jpegBitmap.getPixel(testImageWidth.whiteComponentWidth - 1, 0)
        val whiteColor = Color.WHITE

        Assert.assertEquals(
            "Edge color should be white but it isn't.",
            whiteColor,
            edgeColor
        )
    }

    @Parameterized.Parameter
    @JvmField
    var testParameters: TestParameters? = null

    @Test
    fun testJpegCompression() {
        val parameters = testParameters!!
        Log.i(TAG, "Start test with parameters $parameters")
        assertThatJpegCompressionWorkWithoutArtefacts(
            parameters.image,
            parameters.testImageWidth,
            parameters.jpegQuality
        )
    }

    private companion object {
        val TAG = YuvImageJpegCompressionTest::class.simpleName
        val context: Context = ApplicationProvider.getApplicationContext()

        @Parameterized.Parameters(name = "{index} - {0}")
        @JvmStatic
        fun provideTestParameters(): List<TestParameters> {
            val output = mutableListOf<TestParameters>()
            val widths = listOf(
                TestImageWidth(8, 0),
                TestImageWidth(8, 8),
                TestImageWidth(24, 0),
                TestImageWidth(24, 8)
            )
            val heights = listOf(16)
            val jpegQualities = listOf(50)
            widths.forEach { testImageWidth ->
                heights.forEach { height ->
                    jpegQualities.forEach { quality ->
                        val image = YuvImageGenerator.createNv21YuvImage(
                            testImageWidth,
                            height,
                        )
                        output.add(
                            TestParameters(image, testImageWidth, quality)
                        )
                    }
                }
            }
            return output
        }
    }
}