package test.richmond.movies.model.dto

import test.richmond.movies.model.ImageType

data class ImageDTO(
    val id: String,
    val linkedId: String,
    val url: String = "",
    val imageType: ImageType,
    val imageAlt: String = ""
)
