package test.richmond.movies.service

import java.math.BigDecimal
import java.math.RoundingMode
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import test.richmond.movies.data.entity.CastJobEntity
import test.richmond.movies.data.entity.CrewJobEntity
import test.richmond.movies.data.entity.JobEntity
import test.richmond.movies.data.repository.MovieRepository
import test.richmond.movies.data.entity.MovieEntity
import test.richmond.movies.data.entity.PersonEntity
import test.richmond.movies.data.repository.PersonRepository
import test.richmond.movies.exception.PersonNotFoundException
import test.richmond.movies.model.dto.CastDTO
import test.richmond.movies.model.dto.CrewDTO
import test.richmond.movies.model.dto.MovieCastInput
import test.richmond.movies.model.dto.MovieCrewInput
import test.richmond.movies.model.dto.MovieDTO
import test.richmond.movies.model.dto.MovieInput
import test.richmond.movies.model.dto.MoviesDTO
import test.richmond.movies.model.dto.PersonDTO
import kotlin.Long

@Service
class MovieService(
    val movieRepository: MovieRepository,
    val personRepository: PersonRepository
) {

    fun getMovies(page: Int, pageSize: Int): MoviesDTO {
        val movies = movieRepository.findAll(PageRequest.of(page, pageSize))
        return MoviesDTO(
            movies = movies.stream().map { m -> mapToDTO(m) }.toList(),
            page = page,
            pageSize = pageSize
        )
    }

    fun getMovieById(id: Long): MovieDTO? {
        val movie = movieRepository.findById(id).orElse(null)
        if (movie != null) {
            return mapToDTO(movie)
        }
        return null
    }

    fun addMovie(movie: MovieInput): MovieDTO {
        val result = movieRepository.save(mapToEntity(movie))
        return mapToDTO(result)
    }

    private fun mapToDTO(movieEntity: MovieEntity): MovieDTO {
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

    private fun mapToDTO(castEntity: CastJobEntity): CastDTO {
        return CastDTO(
            personId = castEntity.person.id,
            firstName = castEntity.person.firstName,
            lastName = castEntity.person.lastName,
            sequence = castEntity.sequence,
            characterName = castEntity.characterName,
            jobId = castEntity.id
        )
    }

    private fun mapToDTO(crewEntity: CrewJobEntity): CrewDTO {
        return CrewDTO(
            personId = crewEntity.person.id,
            firstName = crewEntity.person.firstName,
            lastName = crewEntity.person.lastName,
            sequence = crewEntity.sequence,
            jobName = crewEntity.jobName,
            jobId = crewEntity.id
        )
    }

    private fun mapToEntity(movieInput: MovieInput) : MovieEntity {

        val movieEntity = MovieEntity(
            id = 0L,
            title = movieInput.title,
            summary = movieInput.summary,
            synopsis = movieInput.summary,
            rating = BigDecimal(movieInput.rating),
            releaseYear = movieInput.releaseYear
        )

        val personIds = buildSet{
            movieInput.crew.forEach { add(it.personId) }
            movieInput.cast.forEach { add(it.personId) }
        }

        val personMap = personRepository.findAllById(personIds).associateBy { it.id }

        movieInput.crew.stream()
            .map{ v -> mapToEntity(movieEntity, personMap[v.personId], v) }
            .forEach { crew -> movieEntity.crew.add(crew) }

        movieInput.cast.stream()
            .map{ v -> mapToEntity(movieEntity, personMap[v.personId], v) }
            .forEach { cast -> movieEntity.crew.add(cast) }

        return movieEntity
    }

    private fun mapToEntity(movieEntity: MovieEntity, personEntity: PersonEntity?, movieCrewInput: MovieCrewInput) : CrewJobEntity {
        if(personEntity == null) {
            throw PersonNotFoundException(movieCrewInput.personId)
        }
        return CrewJobEntity(
            movie = movieEntity,
            person = personEntity,
            jobName = movieCrewInput.jobName,
            sequence = movieCrewInput.sequence
        )
    }

    private fun mapToEntity(movieEntity: MovieEntity, personEntity: PersonEntity?, movieCastInput: MovieCastInput) : CastJobEntity {
        if(personEntity == null) {
            throw PersonNotFoundException(movieCastInput.personId)
        }
        return CastJobEntity(
            movie = movieEntity,
            person = personEntity,
            characterName = movieCastInput.characterName,
            sequence = movieCastInput.sequence
        )
    }
}
