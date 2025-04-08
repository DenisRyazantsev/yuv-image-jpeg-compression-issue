package jpeg.compression.issue.app.utils

import android.content.ContentValues
import android.content.Context
import android.provider.MediaStore

object FileUtils {

    fun saveJpeg(context: Context, jpegData: ByteArray, fileName: String) {
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/TestDebug")
        }

        val uri = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

        if (uri == null) {
            error("Can't obtain URI")
        }

        val uriStream = context.contentResolver.openOutputStream(uri)

        if (uriStream == null) {
            error("Can't open URI")
        }

        uriStream.use { stream ->
            stream.write(jpegData)
        }
    }

}