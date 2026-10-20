package test.richmond.movies.service

import java.util.Optional
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
import test.richmond.movies.data.entity.PersonEntity
import test.richmond.movies.data.repository.PersonRepository
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

        val result = personService.getPersonById(1L)

        assertThat(result).isNull()
        verify(personRepository).findById(1L)
    }

    @Test
    fun `should return person if person id is valid`() {
        val person = PersonEntity(
            id = 1L,
            firstName = "Peter",
            lastName = "Michael",
            title = "Actor",
        )
        whenever(personRepository.findById(any())).thenReturn(Optional.of(person))

        val result = personService.getPersonById(1L)

        assertThat(result).isNotNull
        assertThat(result!!.id).isEqualTo(1L)
        assertThat(result.firstName).isEqualTo("Peter")
        assertThat(result.lastName).isEqualTo("Michael")
        assertThat(result.title).isEqualTo("Actor")
    }

    @Test
    fun `should return empty list if no person`() {
        val pageable = PageRequest.of(0, 10)
        whenever(personRepository.findAll(any<Pageable>())).thenReturn(PageImpl(listOf(), pageable, 0))

        val result = personService.getPersons(0, 10)

        assertThat(result).isEmpty()
    }

    @Test
    fun `should return list if there are person`() {
        val pageable = PageRequest.of(0, 10)
        val person1 = PersonEntity(
            id = 1L,
            firstName = "Peter",
            lastName = "Michael",
            title = "Actor",
        )
        val person2 = PersonEntity(
            id = 2L,
            firstName = "Jason",
            lastName = "Bear",
            title = "Actor / Director",
        )
        whenever(personRepository.findAll(any<Pageable>())).thenReturn(PageImpl(listOf(person1, person2), pageable, 2))

        val result = personService.getPersons(0, 10)

        assertThat(result).hasSize(2)

        assertThat(result[0].id).isEqualTo(1L)
        assertThat(result[0].firstName).isEqualTo("Peter")
        assertThat(result[0].lastName).isEqualTo("Michael")
        assertThat(result[0].title).isEqualTo("Actor")

        assertThat(result[1].id).isEqualTo(2L)
        assertThat(result[1].firstName).isEqualTo("Jason")
        assertThat(result[1].lastName).isEqualTo("Bear")
        assertThat(result[1].title).isEqualTo("Actor / Director")
    }
}