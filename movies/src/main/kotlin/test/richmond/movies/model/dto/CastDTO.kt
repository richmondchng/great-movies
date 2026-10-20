package test.richmond.movies.model.dto

data class CastDTO(
    val personId: Long,
    val firstName: String,
    val lastName: String?,
    val sequence: Int = 99,
    val characterName: String,
    val jobId: Long
)
