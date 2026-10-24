package test.richmond.movies.service

import java.math.BigDecimal
import java.math.RoundingMode
import java.time.Instant
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.UUID
import java.util.stream.Collectors
import org.slf4j.LoggerFactory
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import test.richmond.movies.controller.PersonController
import test.richmond.movies.data.entity.CastJobEntity
import test.richmond.movies.data.entity.CrewJobEntity
import test.richmond.movies.data.entity.JobEntity
import test.richmond.movies.data.repository.MovieRepository
import test.richmond.movies.data.entity.MovieEntity
import test.richmond.movies.data.entity.PersonEntity
import test.richmond.movies.data.repository.PersonRepository
import test.richmond.movies.exception.PersonNotFoundException
import test.richmond.movies.model.GalleryMap
import test.richmond.movies.model.dto.CastDTO
import test.richmond.movies.model.dto.CrewDTO
import test.richmond.movies.model.dto.ImageDTO
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
    val personRepository: PersonRepository,
    val galleryService: GalleryService
) {

    private val logger = LoggerFactory.getLogger(MovieService::class.java)
    private val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    enum class SortColumn(
        val columnName: String
    ) {
        ID("id"),
        TITLE("title"),
        RATING("rating"),
        RELEASEDATE("releaseDate"),
        RELEASEDYEAR("releaseYear"),
        CREATEDAT("createdAt");

        companion object {
            fun getColumnName(columnName: String): String {
                for (column in entries) {
                    if (column.columnName.equals(columnName, ignoreCase = true)) {
                        return column.columnName
                    }
                }
                return RELEASEDATE.columnName
            }
        }
    }

    fun getMovies(page: Int, pageSize: Int, sortBy: String, sortAsc: Boolean): MoviesDTO {
        val sortDirection = if(sortAsc) Sort.Direction.ASC else Sort.Direction.DESC
        val movies = movieRepository.findAll(PageRequest.of(
            page,
            pageSize,
            sortDirection,
            SortColumn.getColumnName(sortBy)))

        val galleryMap = getGalleryMap(movies.content)

        return MoviesDTO(
            movies = movies.stream().map { m -> mapToDTO(m, galleryMap) }.toList(),
            page = page,
            pageSize = pageSize,
            numberOfElements = movies.numberOfElements,
            totalPages = movies.totalPages,
            totalElements = movies.totalElements
        )
    }

    fun getMovieById(id: String): MovieDTO? {
        val uuid = UUID.fromString(id);
        val movie = movieRepository.findById(uuid).orElse(null)
        if (movie != null) {
            val galleryMap = getGalleryMap(listOf(movie))
            return mapToDTO(movie, galleryMap)
        }
        return null
    }

    fun addMovie(movie: MovieInput): MovieDTO {
        val result = movieRepository.save(mapToEntity(movie))
        val galleryMap = getGalleryMap(listOf(result))
        return mapToDTO(result, galleryMap)
    }

    private fun getGalleryMap(movies: List<MovieEntity>) : GalleryMap {
        if(movies.isEmpty()) {
            return GalleryMap(listOf())
        }

        val ids = mutableSetOf<String>()
        movies.forEach { movie ->
            ids.add(movie.id.toString())
            movie.crew.forEach { crew ->
                ids.add(crew.person.id.toString())
            }
        }
        return galleryService.getImages(ids)
    }

    private fun mapToDTO(movieEntity: MovieEntity, galleryMap: GalleryMap): MovieDTO {
        val crewList = mutableListOf<CrewDTO>()
        val castList = mutableListOf<CastDTO>()

        movieEntity.crew.stream()
            .sorted(Comparator.comparing(JobEntity::sequence))
            .forEach { job ->
                when (job) {
                    is CrewJobEntity -> crewList.add(mapToDTO(job, galleryMap))
                    is CastJobEntity -> castList.add(mapToDTO(job, galleryMap))
                }
            }

        val images: List<ImageDTO> = galleryMap.getImages(movieEntity.id.toString())
        val coverImg: ImageDTO? = galleryMap.getCover(movieEntity.id.toString())

        return MovieDTO(
            id = movieEntity.id.toString(),
            title = movieEntity.title,
            summary = movieEntity.summary,
            synopsis = movieEntity.synopsis,
            releaseYear = movieEntity.releaseYear,
            releaseDate = movieEntity.releaseDate.format(dateTimeFormatter),
            coverImageUrl = coverImg,
            images = images,
            rating = movieEntity.rating.setScale(1, RoundingMode.FLOOR).toPlainString(),
            createdAt = movieEntity.createdAt,
            cast = castList,
            crew = crewList
        )
    }

    private fun mapToDTO(castEntity: CastJobEntity, galleryMap: GalleryMap): CastDTO {
        val coverImg: ImageDTO? = galleryMap.getCover(castEntity.person.id.toString())
        return CastDTO(
            personId = castEntity.person.id.toString(),
            firstName = castEntity.person.firstName,
            lastName = castEntity.person.lastName,
            sequence = castEntity.sequence,
            characterName = castEntity.characterName,
            jobId = castEntity.id.toString(),
            coverImageUrl = coverImg
        )
    }

    private fun mapToDTO(crewEntity: CrewJobEntity, galleryMap: GalleryMap): CrewDTO {
        val coverImg: ImageDTO? = galleryMap.getCover(crewEntity.person.id.toString())
        return CrewDTO(
            personId = crewEntity.person.id.toString(),
            firstName = crewEntity.person.firstName,
            lastName = crewEntity.person.lastName,
            sequence = crewEntity.sequence,
            jobName = crewEntity.jobName,
            jobId = crewEntity.id.toString(),
            coverImageUrl = coverImg,
        )
    }

    private fun mapToEntity(movieInput: MovieInput) : MovieEntity {

        val movieEntity = MovieEntity(
            title = movieInput.title,
            summary = movieInput.summary,
            synopsis = movieInput.summary,
            rating = BigDecimal(movieInput.rating),
            releaseYear = movieInput.releaseYear,
            releaseDate = LocalDate.parse(movieInput.releaseDate)
        )

        val personIds = buildSet{
            movieInput.crew.forEach { add(UUID.fromString(it.personId)) }
            movieInput.cast.forEach { add(UUID.fromString(it.personId)) }
        }

        val personMap = personRepository.findAllById(personIds).associateBy { it.id }

        movieInput.crew.stream()
            .map{ v -> mapToEntity(movieEntity, personMap[UUID.fromString(v.personId)], v) }
            .forEach { crew -> movieEntity.crew.add(crew) }

        movieInput.cast.stream()
            .map{ v -> mapToEntity(movieEntity, personMap[UUID.fromString(v.personId)], v) }
            .forEach { cast -> movieEntity.crew.add(cast) }

        return movieEntity
    }

    private fun mapToEntity(movieEntity: MovieEntity, personEntity: PersonEntity?, movieCrewInput: MovieCrewInput) : CrewJobEntity {
        if(personEntity == null) {
            throw PersonNotFoundException(movieCrewInput.personId)
        }
        return CrewJobEntity(
            id = UUID.randomUUID(),
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
            id = UUID.randomUUID(),
            movie = movieEntity,
            person = personEntity,
            characterName = movieCastInput.characterName,
            sequence = movieCastInput.sequence
        )
    }

}
