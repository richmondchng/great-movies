package test.richmond.movies.data.job

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import java.time.Instant
import org.hibernate.annotations.CreationTimestamp
import test.richmond.movies.model.JobGroup

@Entity
@Table(
    name = "job_type",
    uniqueConstraints = [UniqueConstraint(name = "uq_job_type", columnNames = ["job_group", "job_type"])]
)
class JobTypeEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "job_group")
    val jobGroup: JobGroup = JobGroup.CREW,

    @Column(nullable = false, name = "job_name")
    val jobName: String,

    @CreationTimestamp
    val createdAt: Instant = Instant.now()
)