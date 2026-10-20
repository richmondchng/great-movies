package test.richmond.movies.model.dto

import java.time.Instant

data class MovieDTO(
    val id: Long = 0L,
    val title: String = "",
    val summary: String? = "",
    val synopsis: String? = "",
    val rating: String = "0.0",
    val releaseYear: Int = 0,
    val createdAt: Instant = Instant.now(),
    val cast: List<CastDTO> = listOf(),
    val crew: List<CrewDTO> = listOf()
)
