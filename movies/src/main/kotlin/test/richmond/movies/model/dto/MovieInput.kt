package test.richmond.movies.model.dto

data class MovieInput(
    val title: String,
    val summary: String,
    val releaseYear: Int,
    val rating: String
)
