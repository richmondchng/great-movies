package test.richmond.movies.data.entity

import jakarta.persistence.Column
import jakarta.persistence.DiscriminatorValue
import jakarta.persistence.Entity
import jakarta.persistence.PrimaryKeyJoinColumn
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "job_crew")
@DiscriminatorValue("CREW")
@PrimaryKeyJoinColumn(name = "job_id", referencedColumnName = "id")
class CrewJobEntity(
    movie: MovieEntity,

    person: PersonEntity,

    id: UUID,

    @Column(name = "job_name", nullable = false)
    var jobName: String,

    sequence: Int = 99
) : JobEntity (
    id = id,
    movie = movie,
    person = person,
    sequence = sequence
)