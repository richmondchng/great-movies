package test.richmond.movies.data.movie

import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.MapsId
import jakarta.persistence.Table
import test.richmond.movies.data.job.JobEntity
import test.richmond.movies.data.person.PersonEntity

@Entity
@Table(name = "movie_person_job")
class MoviePersonJob (

    @EmbeddedId
    val id: MoviePersonJobId = MoviePersonJobId(),

    @MapsId("movieId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "movie_id", nullable = false)
    val movie: MovieEntity,

    @MapsId("personId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "person_id", nullable = false)
    val person: PersonEntity,

    @MapsId("jobId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "job_id", nullable = false)
    val job: JobEntity
)