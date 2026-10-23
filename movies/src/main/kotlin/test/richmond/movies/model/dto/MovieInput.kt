package test.richmond.movies.model.dto

data class MovieInput(
    val title: String,
    val summary: String,
    val releaseYear: Int,
    val releaseDate: String,
    val rating: String,
    val cast: List<MovieCastInput> = listOf(),
    val crew: List<MovieCrewInput> = listOf()
)

data class MovieCastInput(
    val personId: Long,
    val characterName: String,
    val sequence: Int = 99
)

data class MovieCrewInput(
    val personId: Long,
    val jobName: String,
    val sequence: Int = 99
)
