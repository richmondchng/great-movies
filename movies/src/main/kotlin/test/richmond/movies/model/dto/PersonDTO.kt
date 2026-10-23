package test.richmond.movies.model.dto

data class PersonDTO(
    val id: String,
    val firstName: String,
    val lastName: String?,
    val title: String?,
    val coverImageUrl: ImageDTO? = null,
    val workedOn: List<WorkedOn> = listOf(),
    val actedIn: List<ActedIn> = listOf()
)

data class WorkedOn (
    val jobId: String,
    val jobName: String,
    val movieId: String,
    val movieName: String,
    val releaseYear: Int,
)

data class ActedIn (
    val jobId: String,
    val characterName: String,
    val movieId: String,
    val movieName: String,
    val releaseYear: Int,
)