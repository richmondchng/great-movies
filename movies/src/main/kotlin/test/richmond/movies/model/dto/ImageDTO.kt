package test.richmond.movies.model.dto

data class ImageDTO(
    val id: String,
    val linkedId: String,
    val url: String = "",
    val imageAlt: String = ""
)
