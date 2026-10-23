package test.richmond.movies.data.repository

import java.util.UUID
import org.springframework.data.jpa.repository.JpaRepository
import test.richmond.movies.data.entity.CastJobEntity

interface CastJobRepository : JpaRepository<CastJobEntity, UUID> {
}