package test.richmond.movies.service

import java.math.BigDecimal
import java.math.RoundingMode
import java.time.Instant
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.UUID
import java.util.stream.Collectors
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
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

    private fun getGalleryMap(movies: List<MovieEntity>) : Map<String, List<ImageDTO>> {
        if(movies.isEmpty()) {
            return mapOf()
        }
        return galleryService.getImages(movies.stream().map { movie -> movie.id.toString() }.collect(
            Collectors.toSet<String>()))
    }

    private fun mapToDTO(movieEntity: MovieEntity, galleryMap: Map<String, List<ImageDTO>>): MovieDTO {
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

        val images: List<ImageDTO> = galleryMap[movieEntity.id.toString()].orEmpty()
        val coverImg: ImageDTO?
        if(images.isEmpty()) {
            coverImg = null
        }else {
            coverImg = images[0]
        }

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

    private fun mapToDTO(castEntity: CastJobEntity): CastDTO {
        return CastDTO(
            personId = castEntity.person.id.toString(),
            firstName = castEntity.person.firstName,
            lastName = castEntity.person.lastName,
            sequence = castEntity.sequence,
            characterName = castEntity.characterName,
            jobId = castEntity.id.toString()
        )
    }

    private fun mapToDTO(crewEntity: CrewJobEntity): CrewDTO {
        return CrewDTO(
            personId = crewEntity.person.id.toString(),
            firstName = crewEntity.person.firstName,
            lastName = crewEntity.person.lastName,
            sequence = crewEntity.sequence,
            jobName = crewEntity.jobName,
            jobId = crewEntity.id.toString()
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
