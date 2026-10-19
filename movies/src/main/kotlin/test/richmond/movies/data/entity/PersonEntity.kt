package test.richmond.movies.data.entity

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
    @Column(name = "created_at")
    val createdAt: Instant = Instant.now(),

    @OneToMany(mappedBy = "person", fetch = FetchType.LAZY)
    val movieJobs: MutableSet<MoviePersonJob> = mutableSetOf()
)