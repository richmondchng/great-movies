package test.richmond.filemgt.model

data class FileDownloadDTO(
    val id: String,
    val bytes: ByteArray,
    val mimeType: String,
    val contentType: String,
)