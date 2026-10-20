package test.richmond.movies.model.dto

data class PersonDTO(
    val id: Long,
    val firstName: String,
    val lastName: String?,
    val title: String?
)