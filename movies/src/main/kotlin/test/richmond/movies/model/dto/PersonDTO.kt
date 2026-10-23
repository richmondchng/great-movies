package test.richmond.movies.model.dto

data class PersonDTO(
    val id: Long,
    val firstName: String,
    val lastName: String?,
    val title: String?,
    val coverImageUrl: ImageDTO? = null,
    val workedOn: List<WorkedOn> = listOf(),
    val actedIn: List<ActedIn> = listOf()
)

data class WorkedOn (
    val jobId: Long,
    val jobName: String,
    val movieId: Long,
    val movieName: String,
    val releaseYear: Int,
)

data class ActedIn (
    val jobId: Long,
    val characterName: String,
    val movieId: Long,
    val movieName: String,
    val releaseYear: Int,
)