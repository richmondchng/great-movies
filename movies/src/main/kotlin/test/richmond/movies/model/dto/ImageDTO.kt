package test.richmond.movies.model.dto

data class ImageDTO(
    val id: String,
    val linkedId: String,
    val imageUrl: String = "",
    val imageAlt: String = ""
)
