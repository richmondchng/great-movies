package test.richmond.movies.model.dto

import java.time.Instant

data class MovieDTO(
    val id: String,
    val title: String = "",
    val summary: String? = "",
    val synopsis: String? = "",
    val rating: String = "0.0",
    val releaseYear: Int = 0,
    val releaseDate: String = "",
    val coverImageUrl: ImageDTO? = null,
    val images: List<ImageDTO> = listOf(),
    val createdAt: Instant = Instant.now(),
    val cast: List<CastDTO> = listOf(),
    val crew: List<CrewDTO> = listOf()
)
