package test.richmond.filemgt.model

data class FileUploadDTO(
    val id: String,
    val linkedId: String,
    val url: String = "",
    val imageType: String
)
