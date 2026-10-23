package test.richmond.movies.data.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import java.time.Instant
import java.util.UUID
import org.hibernate.annotations.CreationTimestamp

@Entity
@Table(name = "person")
class PersonEntity (

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,

    @Column(nullable = false, name = "first_name")
    val firstName: String = "",

    @Column(nullable = true, name = "last_name")
    val lastName: String? = null,

    @Column(nullable = true, name = "title")
    var title: String?,

    @CreationTimestamp
    @Column(name = "created_at")
    val createdAt: Instant = Instant.now(),

    @OneToMany(mappedBy = "person")
    val jobs: MutableList<JobEntity> = mutableListOf()
)