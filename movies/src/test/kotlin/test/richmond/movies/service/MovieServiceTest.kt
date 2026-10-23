package test.richmond.movies.service

import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDate
import java.util.Optional
import java.util.UUID
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import test.richmond.movies.data.entity.CastJobEntity
import test.richmond.movies.data.entity.CrewJobEntity
import test.richmond.movies.data.entity.MovieEntity
import test.richmond.movies.data.entity.PersonEntity
import test.richmond.movies.data.repository.MovieRepository
import test.richmond.movies.data.repository.PersonRepository
import test.richmond.movies.exception.PersonNotFoundException
import test.richmond.movies.model.dto.MovieCastInput
import test.richmond.movies.model.dto.MovieCrewInput
import test.richmond.movies.model.dto.MovieInput
import kotlin.test.Test

@ExtendWith(MockitoExtension::class)
class MovieServiceTest {

    @Mock
    lateinit var movieRepository: MovieRepository

    @Mock
    lateinit var personRepository: PersonRepository

    private lateinit var movieService: MovieService

    @BeforeEach
    fun setUp() {
        movieService = MovieService(movieRepository, personRepository)
    }

    @AfterEach
    fun tearDown() {}

    @Test
    fun `should return empty list if no movies`() {
        val pageable = PageRequest.of(0, 10)
        whenever(movieRepository.findAll(any<Pageable>())).thenReturn(PageImpl(listOf(), pageable, 0))

        val result = movieService.getMovies(0, 10, "releasedDate", true)

        assertThat(result.movies).isEmpty()
        assertThat(result.page).isEqualTo(0)
        assertThat(result.pageSize).isEqualTo(10)
    }

    @Test
    fun `should return list if there are movies`() {
        val pageable = PageRequest.of(0, 10)
        whenever(movieRepository.findAll(any<Pageable>()))
            .thenReturn(PageImpl(
                listOf(
                    constructMovieEntity(
                        id = UUID.fromString("d5171a9d-83a8-4695-9468-396b446b5d6d"),
                        title = "Movie A",
                        summary = "Movie A summary",
                        synopsis = "Movie A synopsis",
                        rating = BigDecimal.valueOf(4.09999),
                        releaseYear = 2020),
                    constructMovieEntity(
                        id = UUID.fromString("3435c7bf-6087-4779-9646-23229e22182a"),
                        title = "Movie B",
                        summary = "Movie B summary",
                        synopsis = "Movie B synopsis",
                        rating = BigDecimal.valueOf(4.09999),
                        releaseYear = 2020)
                ),
                pageable,
                0)
            )

        val result = movieService.getMovies(0, 10, "releasedDate", true)

        assertThat(result.page).isEqualTo(0)
        assertThat(result.pageSize).isEqualTo(10)

        assertThat(result.movies).hasSize(2)
        assertThat(result.movies[0].id).isEqualTo("d5171a9d-83a8-4695-9468-396b446b5d6d")
        assertThat(result.movies[0].title).isEqualTo("Movie A")

        assertThat(result.movies[1].id).isEqualTo("3435c7bf-6087-4779-9646-23229e22182a")
        assertThat(result.movies[1].title).isEqualTo("Movie B")
    }

    @Test
    fun `should not return movie if id is invalid`() {
        whenever(movieRepository.findById(any())).thenReturn(Optional.empty())

        val movie = movieService.getMovieById("3435c7bf-6087-4779-9646-23229e22182a")

        assertThat(movie).isNull()
    }

    @Test
    fun `should return movie with cast and crew by id`() {

        val director = constructPerson(UUID.fromString("cb2423fa-f13a-439a-bf9d-c278e51987d4"), "John", "F", "Director/Actor")
        val writer1 = constructPerson(UUID.fromString("b7a3760a-2b30-4914-b58d-e344eb033875"), "Mark", "T", "Writer")
        val writer2 = constructPerson(UUID.fromString("d7211fa0-7b8c-44eb-b883-e15a27376763"), "Howard", "O", "Writer")
        val actor1 = constructPerson(UUID.fromString("b6b942a9-e71d-47e5-ba7d-7332a0356b33"), "Robert", "B", "Actor")
        val actor2 = constructPerson(UUID.fromString("766cf44b-4a0d-40ee-8a48-59acc65c763a"), "Alice", "A", "Actor")
        val actor3 = constructPerson(UUID.fromString("f7ea9a7e-29c7-414d-9ffa-36e9bad40d93"), "Chris", "H", "Actor")

        val movie = constructMovieEntity(
            id = UUID.fromString("967b128d-1ba4-49c0-b1be-92949fb0c4c5"),
            title = "Movie A",
            summary = "Movie A summary",
            synopsis = "Movie A synopsis",
            rating = BigDecimal.valueOf(4.09999),
            releaseYear = 2020,
        )

        val job1 = constructCrewJob(movie, director, "Director", UUID.fromString("be8bc445-76bb-4cc7-ad5d-d1d5bb1e0394"), 1)
        val job2 = constructCrewJob(movie, writer1, "Writer", UUID.fromString("fba50ac3-cd70-4659-a8bf-e201fc654d63"), seq = 2)
        val job3 = constructCrewJob(movie, writer2, "Writer", UUID.fromString("e181d757-726c-4d9a-bb64-c615300cbcd8"), seq = 3)
        val job4 = constructCastJob(movie, actor1, "Tony Stark", UUID.fromString("cfec035d-8ac9-443b-83c3-0d155c804658"), 11)
        val job5 = constructCastJob(movie, actor2, "Pepper Potts", UUID.fromString("50cc0737-0f60-4e32-a5ff-cb6bd6989013"), 12)
        val job6 = constructCastJob(movie, actor3, "James Rhodes", UUID.fromString("bd684671-2fe8-4b73-a93a-12d2447364bb"), 13)

        movie.crew.add(job3)
        movie.crew.add(job2)
        movie.crew.add(job1)
        movie.crew.add(job5)
        movie.crew.add(job6)
        movie.crew.add(job4)

        whenever(movieRepository.findById(any())).thenReturn(Optional.of(movie))

        val movieDto = movieService.getMovieById("967b128d-1ba4-49c0-b1be-92949fb0c4c5")

        assertThat(movieDto).isNotNull
        assertThat(movieDto?.id).isEqualTo("967b128d-1ba4-49c0-b1be-92949fb0c4c5")
        assertThat(movieDto?.title).isEqualTo("Movie A")
        assertThat(movieDto?.summary).isEqualTo("Movie A summary")
        assertThat(movieDto?.synopsis).isEqualTo("Movie A synopsis")
        assertThat(movieDto?.rating).isEqualTo("4.0")
        assertThat(movieDto?.releaseYear).isEqualTo(2020)
        assertThat(movieDto?.releaseDate).isEqualTo("2020-10-01")

        assertThat(movieDto?.crew).hasSize(3)

        val crew1 = movieDto?.crew?.get(0)
        assertThat(crew1?.personId).isEqualTo("cb2423fa-f13a-439a-bf9d-c278e51987d4")
        assertThat(crew1?.firstName).isEqualTo("John")
        assertThat(crew1?.lastName).isEqualTo("F")
        assertThat(crew1?.sequence).isEqualTo(1)
        assertThat(crew1?.jobId).isEqualTo("be8bc445-76bb-4cc7-ad5d-d1d5bb1e0394")
        assertThat(crew1?.jobName).isEqualTo("Director")

        val crew2 = movieDto?.crew?.get(1)
        assertThat(crew2?.personId).isEqualTo("b7a3760a-2b30-4914-b58d-e344eb033875")
        assertThat(crew2?.firstName).isEqualTo("Mark")
        assertThat(crew2?.lastName).isEqualTo("T")
        assertThat(crew2?.sequence).isEqualTo(2)
        assertThat(crew2?.jobId).isEqualTo("fba50ac3-cd70-4659-a8bf-e201fc654d63")
        assertThat(crew2?.jobName).isEqualTo("Writer")

        val crew3 = movieDto?.crew?.get(2)
        assertThat(crew3?.personId).isEqualTo("d7211fa0-7b8c-44eb-b883-e15a27376763")
        assertThat(crew3?.firstName).isEqualTo("Howard")
        assertThat(crew3?.lastName).isEqualTo("O")
        assertThat(crew3?.sequence).isEqualTo(3)
        assertThat(crew3?.jobId).isEqualTo("e181d757-726c-4d9a-bb64-c615300cbcd8")
        assertThat(crew3?.jobName).isEqualTo("Writer")

        assertThat(movieDto?.cast).hasSize(3)

        val cast1 = movieDto?.cast?.get(0)
        assertThat(cast1?.personId).isEqualTo("b6b942a9-e71d-47e5-ba7d-7332a0356b33")
        assertThat(cast1?.firstName).isEqualTo("Robert")
        assertThat(cast1?.lastName).isEqualTo("B")
        assertThat(cast1?.sequence).isEqualTo(11)
        assertThat(cast1?.jobId).isEqualTo("cfec035d-8ac9-443b-83c3-0d155c804658")
        assertThat(cast1?.characterName).isEqualTo("Tony Stark")

        val cast2 = movieDto?.cast?.get(1)
        assertThat(cast2?.personId).isEqualTo("766cf44b-4a0d-40ee-8a48-59acc65c763a")
        assertThat(cast2?.firstName).isEqualTo("Alice")
        assertThat(cast2?.lastName).isEqualTo("A")
        assertThat(cast2?.sequence).isEqualTo(12)
        assertThat(cast2?.jobId).isEqualTo("50cc0737-0f60-4e32-a5ff-cb6bd6989013")
        assertThat(cast2?.characterName).isEqualTo("Pepper Potts")

        val cast3 = movieDto?.cast?.get(2)
        assertThat(cast3?.personId).isEqualTo("f7ea9a7e-29c7-414d-9ffa-36e9bad40d93")
        assertThat(cast3?.firstName).isEqualTo("Chris")
        assertThat(cast3?.lastName).isEqualTo("H")
        assertThat(cast3?.sequence).isEqualTo(13)
        assertThat(cast3?.jobId).isEqualTo("bd684671-2fe8-4b73-a93a-12d2447364bb")
        assertThat(cast3?.characterName).isEqualTo("James Rhodes")
    }

    @Test
    fun `should throw exception when adding movie if person is not found`() {
        whenever(personRepository.findAllById(any())).thenReturn(listOf())

        val input = MovieInput(
            title = "Movie A",
            summary = "Movie A summary",
            rating = "4.0",
            releaseYear = 2020,
            releaseDate = "2020-10-01",
            cast = listOf(),
            crew = listOf(MovieCrewInput("bd684671-2fe8-4b73-a93a-12d2447364bb", "Director", 2))
        )

        assertThatThrownBy {
            movieService.addMovie(input)
        }.isInstanceOf(PersonNotFoundException::class.java)
            .hasMessage("Person not found with id: bd684671-2fe8-4b73-a93a-12d2447364bb")
    }

    @Test
    fun `should add movie`() {
        val director = constructPerson(UUID.fromString("503c427b-ef21-4c84-90ba-f4e0edb93b3b"), "John", "F", "Director/Actor")
        val actor1 = constructPerson(UUID.fromString("14652cf3-b3dc-457d-881d-65bcc1e2eded"), "Robert", "B", "Actor")
        whenever(personRepository.findAllById(any())).thenReturn(listOf(director, actor1))

        val movie = constructMovieEntity(
            id = UUID.fromString("690816cf-cb15-4451-9869-b73f7143a208"),
            title = "Movie A",
            summary = "Movie A summary",
            synopsis = "Movie A synopsis",
            rating = BigDecimal.valueOf(4.09999),
            releaseYear = 2020,
        )
        whenever(movieRepository.save(any<MovieEntity>())).thenReturn(movie)

        val input = MovieInput(
            title = "Movie A",
            summary = "Movie A summary",
            rating = "4.0",
            releaseYear = 2020,
            releaseDate = "2020-10-01",
            cast = listOf(MovieCastInput(actor1.id.toString(), "Movie character", 1)),
            crew = listOf(MovieCrewInput(director.id.toString(), "Director", 2))
        )

        val result = movieService.addMovie(input)
        assertThat(result).isNotNull
        assertThat(result.id).isEqualTo("690816cf-cb15-4451-9869-b73f7143a208")

        val movieEntityCaptor = argumentCaptor<MovieEntity>()
        verify(movieRepository).save(movieEntityCaptor.capture())

        val personIdCaptor = argumentCaptor<Iterable<UUID>>()
        verify(personRepository).findAllById(personIdCaptor.capture())
        assertThat(personIdCaptor.firstValue).containsExactlyInAnyOrder(
            UUID.fromString("503c427b-ef21-4c84-90ba-f4e0edb93b3b"),
            UUID.fromString("14652cf3-b3dc-457d-881d-65bcc1e2eded"))

        val movieEntity = movieEntityCaptor.firstValue
        assertThat(movieEntity).isNotNull
        assertThat(movieEntity.id).isNotNull
        assertThat(movieEntity.title).isEqualTo("Movie A")
        assertThat(movieEntity.summary).isEqualTo("Movie A summary")
        assertThat(movieEntity.rating).isEqualTo("4.0")
        assertThat(movieEntity.releaseYear).isEqualTo(2020)
        assertThat(movieEntity.releaseDate).isEqualTo(LocalDate.of(2020, 10, 1))

        assertThat(movieEntity.crew).hasSize(2)
        val iterator = movieEntity.crew.stream().sorted(Comparator.comparing { v -> v.sequence }).iterator()

        val crew1 = assertInstanceOf(CastJobEntity::class.java, iterator.next())
        assertThat(crew1.person.id).isEqualTo(UUID.fromString("14652cf3-b3dc-457d-881d-65bcc1e2eded"))
        assertThat(crew1.sequence).isEqualTo(1)
        assertThat(crew1.characterName).isEqualTo("Movie character")

        val crew2 = assertInstanceOf(CrewJobEntity::class.java, iterator.next())
        assertThat(crew2.person.id).isEqualTo(UUID.fromString("503c427b-ef21-4c84-90ba-f4e0edb93b3b"))
        assertThat(crew2.sequence).isEqualTo(2)
        assertThat(crew2.jobName).isEqualTo("Director")
    }

    fun constructMovieEntity(id: UUID,
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
            releaseDate = LocalDate.of(2020, 10, 1),
            createdAt = Instant.now())
    }

    fun constructPerson(id: UUID,
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
                         id: UUID,
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
                         id: UUID,
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