package test.richmond.movies.data.entity

import jakarta.persistence.Column
import jakarta.persistence.DiscriminatorValue
import jakarta.persistence.Entity
import jakarta.persistence.PrimaryKeyJoinColumn
import jakarta.persistence.Table

@Entity
@Table(name = "job_cast")
@DiscriminatorValue("CAST")
@PrimaryKeyJoinColumn(name = "job_id", referencedColumnName = "id")
class CastJobEntity(
    movie: MovieEntity,

    person: PersonEntity,

    @Column(name = "character_name", nullable = false)
    var characterName: String,

    id: Long = 0L,

    sequence : Int = 99
) : JobEntity (
    id = id,
    movie = movie,
    person = person,
    sequence = sequence
)