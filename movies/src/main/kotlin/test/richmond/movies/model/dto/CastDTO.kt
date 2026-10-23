package test.richmond.movies.model.dto

data class CastDTO(
    val personId: String,
    val firstName: String,
    val lastName: String?,
    val sequence: Int = 99,
    val characterName: String,
    val jobId: String,
    val coverImageUrl: ImageDTO? = null
)
