package test.richmond.movies.controller

import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.graphql.test.autoconfigure.GraphQlTest
import org.springframework.context.annotation.Import
import org.springframework.graphql.test.tester.GraphQlTester
import org.springframework.test.context.bean.override.mockito.MockitoBean
import test.richmond.movies.config.GraphQLConfig
import test.richmond.movies.model.dto.PersonDTO
import test.richmond.movies.service.PersonService

@GraphQlTest(PersonController::class)
@Import(GraphQLConfig::class)
class PersonControllerTest @Autowired constructor(
    private val graphQlTester: GraphQlTester
) {
    @MockitoBean
    lateinit var personService: PersonService

    @Test
    fun `should not return anything if person id is invalid`() {
        whenever(personService.getPersonById(any())).thenReturn(null)

        graphQlTester.document("""
            query {
                person(id: 1) {
                    id
                    firstName
                }
            }""".trimIndent()).execute()
            .path("person")
            .valueIsNull()

        verify(personService).getPersonById(1)
    }

    @Test
    fun `should return person if person id is valid`() {
        val person = PersonDTO(
            id = 1L,
            firstName = "John",
            lastName = "Smith",
            title = "Superstar"
        )
        whenever(personService.getPersonById(any())).thenReturn(person)

        graphQlTester.document("""
            query {
                person(id: 1) {
                    id
                    firstName
                    lastName
                    title
                }
            }""".trimIndent()).execute()
            .path("person")
            .matchesJson("""
                {
                    "id": "1",
                    "firstName": "John",
                    "lastName": "Smith",
                    "title": "Superstar"
                }""".trimIndent())

        verify(personService).getPersonById(1)
    }

    @Test
    fun `should only return person last name if person id is valid`() {
        val person = PersonDTO(
            id = 1L,
            firstName = "John",
            lastName = "Smith",
            title = "Superstar"
        )
        whenever(personService.getPersonById(any())).thenReturn(person)

        graphQlTester.document("""
            query {
                person(id: 1) {
                    lastName
                }
            }""".trimIndent()).execute()
            .path("person")
            .matchesJson("""
                {
                    "lastName": "Smith"
                }""".trimIndent())

        verify(personService).getPersonById(1)
    }

    @Test
    fun `should return empty list if no persons`() {
        whenever(personService.getPersons(any(), any())).thenReturn(listOf())

        graphQlTester.document("""
            query {
                persons(page: 1, pageSize: 10) {
                    id
                    firstName
                    lastName
                    title
                }
            }""".trimIndent()).execute()
            .path("persons")
            .matchesJson("""
                []""".trimIndent())

        verify(personService).getPersons(1, 10)
    }

    @Test
    fun `should return list of persons`() {
        val person1 = PersonDTO(
            id = 1L,
            firstName = "John",
            lastName = "Smith",
            title = "Superstar"
        )
        val person2 = PersonDTO(
            id = 2L,
            firstName = "Jane",
            lastName = "Smith",
            title = "Director"
        )
        whenever(personService.getPersons(any(), any())).thenReturn(listOf(person1, person2))

        graphQlTester.document("""
            query {
                persons(page: 1, pageSize: 10) {
                    id
                    firstName
                    lastName
                    title
                }
            }""".trimIndent()).execute()
            .path("persons")
            .matchesJson("""
                [
                    {
                        "id": "1",
                        "firstName": "John",
                        "lastName": "Smith",
                        "title": "Superstar"
                    },
                    {
                        "id": "2",
                        "firstName": "Jane",
                        "lastName": "Smith",
                        "title": "Director"
                    }
                ]""".trimIndent())

        verify(personService).getPersons(1, 10)
    }
}