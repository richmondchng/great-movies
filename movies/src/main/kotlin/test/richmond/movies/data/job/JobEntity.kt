package test.richmond.movies.data.job

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import java.time.Instant
import org.hibernate.annotations.CreationTimestamp
import test.richmond.movies.data.movie.MoviePersonJob

@Entity
@Table(name = "job")
class JobEntity (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    @Column(nullable = false, name = "seq")
    val sequence: Int = 99,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "job_type_id", nullable = true)
    val jobType: JobTypeEntity,

    @CreationTimestamp
    val createdAt: Instant = Instant.now(),

    @OneToMany(mappedBy = "job", fetch = FetchType.LAZY)
    val moviePersonJobs: MutableSet<MoviePersonJob> = mutableSetOf()
)