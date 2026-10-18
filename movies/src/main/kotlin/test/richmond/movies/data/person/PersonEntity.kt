package test.richmond.movies.data.person

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
import test.richmond.movies.data.movie.MoviePersonJob

@Entity
@Table(name = "person")
class PersonEntity (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    @Column(nullable = false, name = "first_name")
    val firstName: String = "",

    @Column(nullable = true, name = "last_name")
    val lastName: String? = null,

    @CreationTimestamp
    val createdAt: Instant = Instant.now(),

    @OneToMany(mappedBy = "person", fetch = FetchType.LAZY)
    val movieJobs: MutableSet<MoviePersonJob> = mutableSetOf()
)