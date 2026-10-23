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
import test.richmond.movies.model.dto.ActedIn
import test.richmond.movies.model.dto.PersonDTO
import test.richmond.movies.model.dto.PersonsDTO
import test.richmond.movies.model.dto.WorkedOn
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
                person(id: "c0e9d32c-146e-4478-84b7-e02c640c5c6b") {
                    id
                    firstName
                }
            }""".trimIndent()).execute()
            .path("person")
            .valueIsNull()

        verify(personService).getPersonById("c0e9d32c-146e-4478-84b7-e02c640c5c6b")
    }

    @Test
    fun `should return person if person id is valid`() {
        val person = PersonDTO(
            id = "c0e9d32c-146e-4478-84b7-e02c640c5c6b",
            firstName = "John",
            lastName = "Smith",
            title = "Superstar",
            workedOn = listOf(
                WorkedOn(
                    jobId = "e60c085c-4ae6-4a62-96fe-4ffea2ab8b03",
                    jobName = "Director",
                    movieId = "da3bb7e2-738f-43db-a8de-70f9a2ff4eb5",
                    movieName = "Movie A",
                    releaseYear = 2021
                )
            ),
            actedIn = listOf(
                ActedIn(
                    jobId = "093e0e4d-3f9e-4ceb-b116-60f1f89c3231",
                    characterName = "Passer-by",
                    movieId = "da3bb7e2-738f-43db-a8de-70f9a2ff4eb5",
                    movieName = "Movie A",
                    releaseYear = 2021
                )
            )
        )
        whenever(personService.getPersonById(any())).thenReturn(person)

        graphQlTester.document("""
            query {
                person(id: "c0e9d32c-146e-4478-84b7-e02c640c5c6b") {
                    id
                    firstName
                    lastName
                    title
                    workedOn {
                        jobId
                        jobName
                        movieId
                        movieName
                        releaseYear
                    }
                    actedIn {
                        jobId
                        characterName
                        movieId
                        movieName
                        releaseYear
                    }
                }
            }""".trimIndent()).execute()
            .path("person")
            .matchesJson("""
                {
                    "id": "c0e9d32c-146e-4478-84b7-e02c640c5c6b",
                    "firstName": "John",
                    "lastName": "Smith",
                    "title": "Superstar",
                    "workedOn": [
                        {
                            "jobId": "e60c085c-4ae6-4a62-96fe-4ffea2ab8b03",
                            "jobName": "Director",
                            "movieId": "da3bb7e2-738f-43db-a8de-70f9a2ff4eb5",
                            "movieName": "Movie A",
                            "releaseYear": 2021
                        }
                    ],
                    "actedIn": [
                        {
                            "jobId": "093e0e4d-3f9e-4ceb-b116-60f1f89c3231",
                            "characterName": "Passer-by",
                            "movieId": "da3bb7e2-738f-43db-a8de-70f9a2ff4eb5",
                            "movieName": "Movie A",
                            "releaseYear": 2021
                        }
                    ]
                }""".trimIndent())

        verify(personService).getPersonById("c0e9d32c-146e-4478-84b7-e02c640c5c6b")
    }

    @Test
    fun `should only return person last name if person id is valid`() {
        val person = PersonDTO(
            id = "c0e9d32c-146e-4478-84b7-e02c640c5c6b",
            firstName = "John",
            lastName = "Smith",
            title = "Superstar"
        )
        whenever(personService.getPersonById(any())).thenReturn(person)

        graphQlTester.document("""
            query {
                person(id: "c0e9d32c-146e-4478-84b7-e02c640c5c6b") {
                    lastName
                }
            }""".trimIndent()).execute()
            .path("person")
            .matchesJson("""
                {
                    "lastName": "Smith"
                }""".trimIndent())

        verify(personService).getPersonById("c0e9d32c-146e-4478-84b7-e02c640c5c6b")
    }

    @Test
    fun `should return empty list if no persons`() {
        whenever(personService.getPersons(any(), any())).thenReturn(
            PersonsDTO(
                persons = listOf(),
                page = 0,
                pageSize = 20,
                numberOfElements = 0,
                totalPages = 0,
                totalElements = 0
            )
        )

        graphQlTester.document("""
            query {
                persons {
                    persons {
                        id
                        firstName
                        lastName
                        title
                    }
                    page
                    pageSize
                }
            }""".trimIndent()).execute()
            .path("persons")
            .matchesJson("""
                {
                    "persons": [],
                    "page": 0,
                    "pageSize": 20
                }""".trimIndent())

        verify(personService).getPersons(0, 20)
    }

    @Test
    fun `should return list of persons`() {
        val person1 = PersonDTO(
            id = "c0e9d32c-146e-4478-84b7-e02c640c5c6b",
            firstName = "John",
            lastName = "Smith",
            title = "Superstar"
        )
        val person2 = PersonDTO(
            id = "3d4e0114-a34b-49c7-93e5-346affe298d7",
            firstName = "Jane",
            lastName = "Smith",
            title = "Director"
        )
        whenever(personService.getPersons(any(), any())).thenReturn(
            PersonsDTO(
                persons = listOf(person1, person2),
                page = 1,
                pageSize = 10,
                numberOfElements = 2,
                totalPages = 0,
                totalElements = 2
            )
        )

        graphQlTester.document("""
            query {
                persons(page: 1, pageSize: 10) {
                    persons {
                        id
                        firstName
                        lastName
                        title
                    }
                    page
                    pageSize
                }
            }""".trimIndent()).execute()
            .path("persons")
            .matchesJson("""
                {
                    "persons": [
                        {
                            "id": "c0e9d32c-146e-4478-84b7-e02c640c5c6b",
                            "firstName": "John",
                            "lastName": "Smith",
                            "title": "Superstar"
                        },
                        {
                            "id": "3d4e0114-a34b-49c7-93e5-346affe298d7",
                            "firstName": "Jane",
                            "lastName": "Smith",
                            "title": "Director"
                        }
                    ],
                    page: 1,
                    pageSize: 10
                }""".trimIndent())

        verify(personService).getPersons(1, 10)
    }

    @Test
    fun `should return person after add`() {
        whenever(personService.addPerson(any())).thenReturn(
            PersonDTO(
                id = "3d4e0114-a34b-49c7-93e5-346affe298d7",
                firstName = "John",
                lastName = "Smith",
                title = "Superstar"
            )
        )

        graphQlTester.document("""
            mutation {
                addPerson(person: {
                    firstName: "John"
                    lastName: "Smith"
                    title: "Superstar"
                }) {
                    id
                    firstName
                    lastName
                    title
                }
            }""".trimIndent()).execute()
            .path("addPerson")
            .matchesJson("""
                {
                    "id": "3d4e0114-a34b-49c7-93e5-346affe298d7",
                    "firstName": "John",
                    "lastName": "Smith",
                    "title": "Superstar"
                }""".trimIndent())
    }
}