package test.richmond.movies.data.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDate
import java.util.UUID
import org.hibernate.annotations.CreationTimestamp

@Entity
@Table(name = "movie")
class MovieEntity (

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,

    @Column(nullable = false, name = "title")
    var title: String = "",

    @Column(nullable = true, name = "summary")
    var summary: String?,

    @Column(nullable = true, name = "synopsis")
    var synopsis: String?,

    @Column(nullable = false, name = "release_year")
    var releaseYear: Int,

    @Column(nullable = false, name = "release_date")
    var releaseDate: LocalDate,

    @Column(nullable = false, name = "rating")
    var rating: BigDecimal = BigDecimal.ZERO,

    @CreationTimestamp
    @Column(name = "created_at")
    var createdAt: Instant = Instant.now(),

    @OneToMany(mappedBy = "movie", cascade = [CascadeType.ALL], orphanRemoval = true)
    val crew: MutableList<JobEntity> = mutableListOf()
)