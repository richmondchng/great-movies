package test.richmond.movies.model.dto

data class PersonInput(
    val firstName: String,
    val lastName: String = "",
    val title: String = ""
)
