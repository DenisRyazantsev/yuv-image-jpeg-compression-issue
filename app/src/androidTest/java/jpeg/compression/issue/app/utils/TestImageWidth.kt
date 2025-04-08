package jpeg.compression.issue.app.utils

data class TestImageWidth(
    val whiteComponentWidth: Int,
    val blackComponentWidth: Int,
) {
    val overallWidth = whiteComponentWidth + blackComponentWidth
}