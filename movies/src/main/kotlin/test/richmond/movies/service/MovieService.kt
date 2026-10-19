package test.richmond.movies.service

import java.math.RoundingMode
import org.springframework.stereotype.Service
import test.richmond.movies.data.entity.CastJobEntity
import test.richmond.movies.data.entity.CrewJobEntity
import test.richmond.movies.data.entity.JobEntity
import test.richmond.movies.data.repository.MovieRepository
import test.richmond.movies.data.entity.MovieEntity
import test.richmond.movies.model.dto.CastDTO
import test.richmond.movies.model.dto.CrewDTO
import test.richmond.movies.model.dto.MovieDTO
import kotlin.Long

@Service
class MovieService(
    var movieRepository: MovieRepository
) {

    fun findMovieById(id: Long): MovieDTO? {
        val movie = movieRepository.findById(id).orElse(null)
        if (movie != null) {
            return mapToDTO(movie)
        }
        return null
    }

    fun mapToDTO(movieEntity: MovieEntity): MovieDTO {
        val crewList = mutableListOf<CrewDTO>()
        val castList = mutableListOf<CastDTO>()

        movieEntity.crew.stream()
            .sorted(Comparator.comparing(JobEntity::sequence))
            .forEach { job ->
                when (job) {
                    is CrewJobEntity -> crewList.add(mapToDTO(job))
                    is CastJobEntity -> castList.add(mapToDTO(job))
                }
            }
        return MovieDTO(
            id = movieEntity.id,
            title = movieEntity.title,
            summary = movieEntity.summary,
            synopsis = movieEntity.synopsis,
            releaseYear = movieEntity.releaseYear,
            rating = movieEntity.rating.setScale(1, RoundingMode.FLOOR).toPlainString(),
            createdAt = movieEntity.createdAt,
            cast = castList,
            crew = crewList
        )
    }

    fun mapToDTO(castEntity: CastJobEntity): CastDTO {
        return CastDTO(
            personId = castEntity.person.id,
            firstName = castEntity.person.firstName,
            lastName = castEntity.person.lastName,
            sequence = castEntity.sequence,
            characterName = castEntity.characterName,
            jobId = castEntity.id
        )
    }

    fun mapToDTO(crewEntity: CrewJobEntity): CrewDTO {
        return CrewDTO(
            personId = crewEntity.person.id,
            firstName = crewEntity.person.firstName,
            lastName = crewEntity.person.lastName,
            sequence = crewEntity.sequence,
            jobName = crewEntity.jobName,
            jobId = crewEntity.id
        )
    }
}
