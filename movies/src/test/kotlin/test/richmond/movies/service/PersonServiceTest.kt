package test.richmond.movies.service

import java.time.Instant
import java.time.LocalDate
import java.util.Optional
import java.util.UUID
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import test.richmond.movies.data.entity.CastJobEntity
import test.richmond.movies.data.entity.CrewJobEntity
import test.richmond.movies.data.entity.JobEntity
import test.richmond.movies.data.entity.MovieEntity
import test.richmond.movies.data.entity.PersonEntity
import test.richmond.movies.data.repository.PersonRepository
import test.richmond.movies.model.dto.PersonInput
import kotlin.test.Test

@ExtendWith(MockitoExtension::class)
class PersonServiceTest {

    @Mock
    lateinit var personRepository: PersonRepository

    private lateinit var personService: PersonService

    @BeforeEach
    fun setUp() {
        personService = PersonService(personRepository)
    }

    @AfterEach
    fun tearDown() {}

    @Test
    fun `should throw exception if person does not exist`() {
        whenever(personRepository.findById(any())).thenReturn(Optional.empty())

        val result = personService.getPersonById("766cf44b-4a0d-40ee-8a48-59acc65c763a")

        assertThat(result).isNull()
        verify(personRepository).findById(UUID.fromString("766cf44b-4a0d-40ee-8a48-59acc65c763a"))
    }

    @Test
    fun `should return person if person id is valid`() {
        val person = PersonEntity(
            id = UUID.fromString("cb2423fa-f13a-439a-bf9d-c278e51987d4"),
            firstName = "Peter",
            lastName = "Michael",
            title = "Actor")
        person.jobs.add(
            CrewJobEntity(
                id = UUID.fromString("503c427b-ef21-4c84-90ba-f4e0edb93b3b"),
                jobName = "Writer",
                movie = MovieEntity(
                    id = UUID.fromString("bd684671-2fe8-4b73-a93a-12d2447364bb"),
                    title = "Movie A",
                    summary = "",
                    synopsis = "",
                    releaseYear = 2022,
                    releaseDate = LocalDate.of(2022, 10, 1)
                ),
                person = person
            )
        )
        person.jobs.add(
            CastJobEntity(
                id = UUID.fromString("967b128d-1ba4-49c0-b1be-92949fb0c4c5"),
                characterName = "Movie star",
                movie = MovieEntity(
                    id = UUID.fromString("50cc0737-0f60-4e32-a5ff-cb6bd6989013"),
                    title = "Movie B",
                    summary = "",
                    synopsis = "",
                    releaseYear = 2021,
                    releaseDate = LocalDate.of(2022, 10, 1)
                ),
                person = person
            )
        )
        person.jobs.add(
            CrewJobEntity(
                id = UUID.fromString("55d1008f-eb19-4e90-87df-c4cbdb2914ee"),
                jobName = "Lighting",
                movie = MovieEntity(
                    id = UUID.fromString("994d704e-ac7d-431e-b282-090fef4a6a2a"),
                    title = "Movie C",
                    summary = "",
                    synopsis = "",
                    releaseYear = 2023,
                    releaseDate = LocalDate.of(2022, 10, 1)
                ),
                person = person
            )
        )

        whenever(personRepository.findById(any())).thenReturn(Optional.of(person))

        val result = personService.getPersonById("cb2423fa-f13a-439a-bf9d-c278e51987d4")

        assertThat(result).isNotNull
        assertThat(result!!.id).isEqualTo("cb2423fa-f13a-439a-bf9d-c278e51987d4")
        assertThat(result.firstName).isEqualTo("Peter")
        assertThat(result.lastName).isEqualTo("Michael")
        assertThat(result.title).isEqualTo("Actor")

        assertThat(result.workedOn).hasSize(2)
        assertThat(result.workedOn[0].jobId).isEqualTo("55d1008f-eb19-4e90-87df-c4cbdb2914ee")
        assertThat(result.workedOn[0].jobName).isEqualTo("Lighting")
        assertThat(result.workedOn[0].movieId).isEqualTo("994d704e-ac7d-431e-b282-090fef4a6a2a")
        assertThat(result.workedOn[0].movieName).isEqualTo("Movie C")
        assertThat(result.workedOn[0].releaseYear).isEqualTo(2023)

        assertThat(result.workedOn[1].jobId).isEqualTo("503c427b-ef21-4c84-90ba-f4e0edb93b3b")
        assertThat(result.workedOn[1].jobName).isEqualTo("Writer")
        assertThat(result.workedOn[1].movieId).isEqualTo("bd684671-2fe8-4b73-a93a-12d2447364bb")
        assertThat(result.workedOn[1].movieName).isEqualTo("Movie A")
        assertThat(result.workedOn[1].releaseYear).isEqualTo(2022)

        assertThat(result.actedIn).hasSize(1)
        assertThat(result.actedIn[0].jobId).isEqualTo("967b128d-1ba4-49c0-b1be-92949fb0c4c5")
        assertThat(result.actedIn[0].characterName).isEqualTo("Movie star")
        assertThat(result.actedIn[0].movieId).isEqualTo("50cc0737-0f60-4e32-a5ff-cb6bd6989013")
        assertThat(result.actedIn[0].movieName).isEqualTo("Movie B")
        assertThat(result.actedIn[0].releaseYear).isEqualTo(2021)
    }

    @Test
    fun `should return empty list if no person`() {
        val pageable = PageRequest.of(0, 10)
        whenever(personRepository.findAll(any<Pageable>())).thenReturn(PageImpl(listOf(), pageable, 0))

        val result = personService.getPersons(0, 10)

        assertThat(result.page).isEqualTo(0)
        assertThat(result.pageSize).isEqualTo(10)
        assertThat(result.persons).isEmpty()
    }

    @Test
    fun `should return list if there are person`() {
        val pageable = PageRequest.of(0, 10)
        val person1 = PersonEntity(
            id = UUID.fromString("e181d757-726c-4d9a-bb64-c615300cbcd8"),
            firstName = "Peter",
            lastName = "Michael",
            title = "Actor",
        )
        val person2 = PersonEntity(
            id = UUID.fromString("fba50ac3-cd70-4659-a8bf-e201fc654d63"),
            firstName = "Jason",
            lastName = "Bear",
            title = "Actor / Director",
        )
        whenever(personRepository.findAll(any<Pageable>())).thenReturn(PageImpl(listOf(person1, person2), pageable, 2))

        val result = personService.getPersons(0, 10)

        assertThat(result.page).isEqualTo(0)
        assertThat(result.pageSize).isEqualTo(10)
        assertThat(result.persons).hasSize(2)

        assertThat(result.persons[0].id).isEqualTo("e181d757-726c-4d9a-bb64-c615300cbcd8")
        assertThat(result.persons[0].firstName).isEqualTo("Peter")
        assertThat(result.persons[0].lastName).isEqualTo("Michael")
        assertThat(result.persons[0].title).isEqualTo("Actor")

        assertThat(result.persons[1].id).isEqualTo("fba50ac3-cd70-4659-a8bf-e201fc654d63")
        assertThat(result.persons[1].firstName).isEqualTo("Jason")
        assertThat(result.persons[1].lastName).isEqualTo("Bear")
        assertThat(result.persons[1].title).isEqualTo("Actor / Director")
    }

    @Test
    fun `should add person`() {
        whenever(personRepository.save<PersonEntity>(any())).thenReturn(
            PersonEntity(
                id = UUID.fromString("fba50ac3-cd70-4659-a8bf-e201fc654d63"),
                firstName = "Peter",
                lastName = "Michael",
                title = "Actor",
                createdAt = Instant.now()
            )
        )

        val result = personService.addPerson(PersonInput(
            firstName = "Peter",
            lastName = "Michael",
            title = "Actor",
        ))

        assertThat(result.id).isEqualTo("fba50ac3-cd70-4659-a8bf-e201fc654d63")
        assertThat(result.firstName).isEqualTo("Peter")
        assertThat(result.lastName).isEqualTo("Michael")
        assertThat(result.title).isEqualTo("Actor")
    }
}