package test.richmond.movies.data.repository

import org.springframework.data.jpa.repository.JpaRepository
import test.richmond.movies.data.entity.CrewJobEntity

interface CrewJobRepository : JpaRepository<CrewJobEntity, Long> {
}