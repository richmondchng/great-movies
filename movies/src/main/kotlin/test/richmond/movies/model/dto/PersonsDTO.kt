package test.richmond.movies.model.dto

data class PersonsDTO(
    val persons: List<PersonDTO>,
    val page: Int,
    val pageSize: Int,
    val numberOfElements: Int,
    val totalPages: Int,
    val totalElements: Long
)
