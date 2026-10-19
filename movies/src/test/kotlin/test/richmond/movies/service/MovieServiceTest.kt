package test.richmond.movies.service

import java.math.BigDecimal
import java.time.Instant
import java.util.Optional
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import test.richmond.movies.data.entity.CastJobEntity
import test.richmond.movies.data.entity.CrewJobEntity
import test.richmond.movies.data.entity.MovieEntity
import test.richmond.movies.data.entity.PersonEntity
import test.richmond.movies.data.repository.MovieRepository
import test.richmond.movies.model.dto.MovieInput

@ExtendWith(MockitoExtension::class)
class MovieServiceTest {

    @Mock
    lateinit var movieRepository: MovieRepository

    private lateinit var movieService: MovieService

    @BeforeEach
    fun setUp() {
        movieService = MovieService(movieRepository)
    }

    @AfterEach
    fun tearDown() {}

    @Test
    fun `should not return movie if id is invalid`() {
        whenever(movieRepository.findById(any())).thenReturn(Optional.empty())

        val movie = movieService.findMovieById(1L)

        assertThat(movie).isNull()
    }

    @Test
    fun `should return movie with cast and crew by id`() {

        val director = constructPerson(1L, "John", "F", "Director/Actor")
        val writer1 = constructPerson(2L, "Mark", "T", "Writer")
        val writer2 = constructPerson(3L, "Howard", "O", "Writer")
        val actor1 = constructPerson(4L, "Robert", "B", "Actor")
        val actor2 = constructPerson(5L, "Alice", "A", "Actor")
        val actor3 = constructPerson(6L, "Chris", "H", "Actor")

        val movie = constructMovieEntity(
            id = 1L,
            title = "Movie A",
            summary = "Movie A summary",
            synopsis = "Movie A synopsis",
            rating = BigDecimal.valueOf(4.09999),
            releaseYear = 2020,
        )

        val job1 = constructCrewJob(movie, director, "Director", 11L, 1)
        val job2 = constructCrewJob(movie, writer1, "Writer", 12L, seq = 2)
        val job3 = constructCrewJob(movie, writer2, "Writer", 13L, seq = 3)
        val job4 = constructCastJob(movie, actor1, "Tony Stark", 14L, 11)
        val job5 = constructCastJob(movie, actor2, "Pepper Potts", 15L, 12)
        val job6 = constructCastJob(movie, actor3, "James Rhodes", 16L, 13)

        movie.crew.add(job3)
        movie.crew.add(job2)
        movie.crew.add(job1)
        movie.crew.add(job5)
        movie.crew.add(job6)
        movie.crew.add(job4)

        whenever(movieRepository.findById(any())).thenReturn(Optional.of(movie))

        val movieDto = movieService.findMovieById(1L)

        assertThat(movieDto).isNotNull
        assertThat(movieDto?.id).isEqualTo(1L)
        assertThat(movieDto?.title).isEqualTo("Movie A")
        assertThat(movieDto?.summary).isEqualTo("Movie A summary")
        assertThat(movieDto?.synopsis).isEqualTo("Movie A synopsis")
        assertThat(movieDto?.rating).isEqualTo("4.0")
        assertThat(movieDto?.releaseYear).isEqualTo(2020)

        assertThat(movieDto?.crew).hasSize(3)

        val crew1 = movieDto?.crew?.get(0)
        assertThat(crew1?.personId).isEqualTo(1L)
        assertThat(crew1?.firstName).isEqualTo("John")
        assertThat(crew1?.lastName).isEqualTo("F")
        assertThat(crew1?.sequence).isEqualTo(1)
        assertThat(crew1?.jobId).isEqualTo(11L)
        assertThat(crew1?.jobName).isEqualTo("Director")

        val crew2 = movieDto?.crew?.get(1)
        assertThat(crew2?.personId).isEqualTo(2L)
        assertThat(crew2?.firstName).isEqualTo("Mark")
        assertThat(crew2?.lastName).isEqualTo("T")
        assertThat(crew2?.sequence).isEqualTo(2)
        assertThat(crew2?.jobId).isEqualTo(12L)
        assertThat(crew2?.jobName).isEqualTo("Writer")

        val crew3 = movieDto?.crew?.get(2)
        assertThat(crew3?.personId).isEqualTo(3L)
        assertThat(crew3?.firstName).isEqualTo("Howard")
        assertThat(crew3?.lastName).isEqualTo("O")
        assertThat(crew3?.sequence).isEqualTo(3)
        assertThat(crew3?.jobId).isEqualTo(13L)
        assertThat(crew3?.jobName).isEqualTo("Writer")

        assertThat(movieDto?.cast).hasSize(3)

        val cast1 = movieDto?.cast?.get(0)
        assertThat(cast1?.personId).isEqualTo(4L)
        assertThat(cast1?.firstName).isEqualTo("Robert")
        assertThat(cast1?.lastName).isEqualTo("B")
        assertThat(cast1?.sequence).isEqualTo(11)
        assertThat(cast1?.jobId).isEqualTo(14L)
        assertThat(cast1?.characterName).isEqualTo("Tony Stark")

        val cast2 = movieDto?.cast?.get(1)
        assertThat(cast2?.personId).isEqualTo(5L)
        assertThat(cast2?.firstName).isEqualTo("Alice")
        assertThat(cast2?.lastName).isEqualTo("A")
        assertThat(cast2?.sequence).isEqualTo(12)
        assertThat(cast2?.jobId).isEqualTo(15L)
        assertThat(cast2?.characterName).isEqualTo("Pepper Potts")

        val cast3 = movieDto?.cast?.get(2)
        assertThat(cast3?.personId).isEqualTo(6L)
        assertThat(cast3?.firstName).isEqualTo("Chris")
        assertThat(cast3?.lastName).isEqualTo("H")
        assertThat(cast3?.sequence).isEqualTo(13)
        assertThat(cast3?.jobId).isEqualTo(16L)
        assertThat(cast3?.characterName).isEqualTo("James Rhodes")
    }

    @Test
    fun `should add movie if id is 0`() {
        val movie = constructMovieEntity(
            id = 2L,
            title = "Movie A",
            summary = "Movie A summary",
            synopsis = "Movie A synopsis",
            rating = BigDecimal.valueOf(4.09999),
            releaseYear = 2020,
        )
        whenever(movieRepository.save(any<MovieEntity>())).thenReturn(movie)

        val input = MovieInput(id = 0,
            title = "Movie A",
            summary = "Movie A summary",
            rating = "4.0",
            releaseYear = 2020)
        val result = movieService.addMovie(input)

        assertThat(result).isNotNull
        assertThat(result.id).isEqualTo(2L)
        assertThat(result.title).isEqualTo("Movie A")
        assertThat(result.summary).isEqualTo("Movie A summary")
        assertThat(result.rating).isEqualTo("4.0")
        assertThat(result.releaseYear).isEqualTo(2020)
    }

    fun constructMovieEntity(id: Long,
                             title: String = "",
                             summary: String = "",
                             synopsis: String = "",
                             rating: BigDecimal = BigDecimal.ZERO,
                             releaseYear: Int = 2026
    ) : MovieEntity {
        return MovieEntity(
            id = id,
            title = title,
            summary = summary,
            synopsis = synopsis,
            rating = rating,
            releaseYear = releaseYear,
            createdAt = Instant.now())
    }

    fun constructPerson(id: Long,
                        firstName: String,
                        lastName: String = "",
                        title: String = ""
    ) : PersonEntity {
        return PersonEntity(
            id = id,
            firstName = firstName,
            lastName = lastName,
            title = title,
            createdAt = Instant.now()
        )
    }

    fun constructCrewJob(movie: MovieEntity,
                         person: PersonEntity,
                         jobName: String = "",
                         id: Long = 0L,
                         seq: Int = 99) : CrewJobEntity {
        return CrewJobEntity(
            id = id,
            person = person,
            movie = movie,
            jobName = jobName,
            sequence = seq);
    }

    fun constructCastJob(movie: MovieEntity,
                         person: PersonEntity,
                         characterName: String = "",
                         id: Long = 0L,
                         seq: Int = 99) : CastJobEntity {
        return CastJobEntity(
            id = id,
            person = person,
            movie = movie,
            characterName = characterName,
            sequence = seq
        );
    }

}