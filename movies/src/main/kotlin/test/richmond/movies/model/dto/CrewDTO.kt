package test.richmond.movies.model.dto

data class CrewDTO(
    val personId: String,
    val firstName: String,
    val lastName: String?,
    val sequence: Int = 99,
    val jobName: String,
    val jobId: String,
    val coverImageUrl: ImageDTO? = null
)
