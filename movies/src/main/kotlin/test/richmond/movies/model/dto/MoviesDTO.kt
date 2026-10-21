package test.richmond.movies.model.dto

data class MoviesDTO(
    val movies: List<MovieDTO>,
    val page: Int,
    val pageSize: Int,
    val numberOfElements: Int,
    val totalPages: Int,
    val totalElements: Long
)
