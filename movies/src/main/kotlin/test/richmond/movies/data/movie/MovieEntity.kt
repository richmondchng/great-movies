package test.richmond.movies.data.movie

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import java.time.Instant
import org.hibernate.annotations.CreationTimestamp

@Entity
@Table(name = "movie")
class MovieEntity (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    @Column(nullable = false, name = "title")
    val title: String,

    @Column(nullable = true, name = "summary")
    val summary: String?,

    @Column(nullable = true, name = "synopsis")
    val synopsis: String?,

    @CreationTimestamp
    val createdAt: Instant = Instant.now(),

    @OneToMany(mappedBy = "movie", fetch = FetchType.LAZY)
    val peopleJobs: MutableSet<MoviePersonJob> = mutableSetOf()
)
