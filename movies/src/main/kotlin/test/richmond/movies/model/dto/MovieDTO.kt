package test.richmond.movies.model.dto

import java.time.Instant
import java.time.LocalDate
import lombok.Builder

data class MovieDTO(
    var id : Long,
    var title : String,
    var summary : String,
    var synopsis : String,
    var releaseDate : LocalDate,
    var createdAt: Instant
)
