package test.richmond.movies.data.entity

import jakarta.persistence.Column
import jakarta.persistence.DiscriminatorValue
import jakarta.persistence.Entity
import jakarta.persistence.PrimaryKeyJoinColumn
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "job_cast")
@DiscriminatorValue("CAST")
@PrimaryKeyJoinColumn(name = "job_id", referencedColumnName = "id")
class CastJobEntity(
    movie: MovieEntity,

    person: PersonEntity,

    @Column(name = "character_name", nullable = false)
    var characterName: String,

    id: UUID,

    sequence : Int = 99
) : JobEntity (
    id = id,
    movie = movie,
    person = person,
    sequence = sequence
)