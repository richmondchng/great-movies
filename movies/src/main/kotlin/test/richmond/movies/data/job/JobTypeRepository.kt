package test.richmond.movies.data.job

import org.springframework.data.jpa.repository.JpaRepository

interface JobTypeRepository : JpaRepository<JobTypeEntity, Long> {
}