package test.richmond.movies.data.movie

import jakarta.persistence.Column
import java.io.Serializable

data class MoviePersonJobId(
    @Column(name = "movie_id")
    val movieId: Long = 0,

    @Column(name = "person_id")
    val personId: Long = 0,

    @Column(name = "job_id")
    val jobId: Long = 0
) : Serializable
