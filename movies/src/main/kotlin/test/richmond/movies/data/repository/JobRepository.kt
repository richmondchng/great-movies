package test.richmond.movies.data.repository

import org.springframework.data.jpa.repository.JpaRepository
import test.richmond.movies.data.entity.JobEntity

interface JobRepository : JpaRepository<JobEntity, Long> {
}