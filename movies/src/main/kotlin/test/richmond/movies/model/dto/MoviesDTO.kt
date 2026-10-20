package test.richmond.movies.model.dto

data class MoviesDTO(
    val movies: List<MovieDTO>,
    val page: Int,
    val pageSize: Int
)
