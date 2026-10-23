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
import test.richmond.movies.model.dto.CastDTO
import test.richmond.movies.model.dto.CrewDTO
import test.richmond.movies.model.dto.MovieDTO
import test.richmond.movies.model.dto.MoviesDTO
import test.richmond.movies.service.MovieService
import kotlin.Int

@GraphQlTest(MovieController::class)
@Import(GraphQLConfig::class)
class MovieControllerTest @Autowired constructor(
    private val graphQlTester: GraphQlTester
) {

    @MockitoBean
    lateinit var movieService: MovieService

    @Test
    fun `should return empty list if no movies`() {
        whenever(movieService.getMovies(any(), any(),any(), any())).thenReturn(
            MoviesDTO(
                movies = listOf(),
                page = 0,
                pageSize = 20,
                numberOfElements = 0,
                totalPages = 0,
                totalElements = 0
            )
        )

        graphQlTester.document("""
            query {
                movies {
                    movies {
                        id
                        title
                    }
                    page
                    pageSize
                }
            }""".trimIndent()).execute()
            .path("movies")
            .matchesJson("""
                {
                    "movies": [],
                    "page": 0,
                    "pageSize": 20
                }""".trimIndent())

        verify(movieService).getMovies(0, 20, "releaseDate", true)
    }

    @Test
    fun `should return list of movies`() {
        whenever(movieService.getMovies(any(), any(), any(), any()))
            .thenReturn(
                MoviesDTO(
                    movies = listOf(
                        constructMovieDTO(id = "c0e9d32c-146e-4478-84b7-e02c640c5c6b", title = "Movie 1"),
                        constructMovieDTO(id = "53d902aa-d37c-4c55-ac6a-8759e24b3041", title = "Movie 2")
                    ),
                    page = 1,
                    pageSize = 10,
                    numberOfElements = 2,
                    totalPages = 0,
                    totalElements = 2
                )
            )

        graphQlTester.document("""
            query {
                movies(page: 1, pageSize: 10) {
                    movies {
                        id
                        title
                    }
                    page
                    pageSize
                }
            }""".trimIndent()).execute()
            .path("movies")
            .matchesJson("""
                {
                    "movies": [
                        {
                            "id": "c0e9d32c-146e-4478-84b7-e02c640c5c6b",
                            "title": "Movie 1"
                        },
                        {
                            "id": "53d902aa-d37c-4c55-ac6a-8759e24b3041",
                            "title": "Movie 2"
                        }
                    ],
                    "page": 1,
                    "pageSize": 10
                }""".trimIndent())

        verify(movieService).getMovies(1, 10, "releaseDate", true)
    }

    @Test
    fun `should not return anything if movie id is invalid`() {
        whenever(movieService.getMovieById(any())).thenReturn(null)

        graphQlTester.document("""
            query {
                movie(id: "2f251eae-09b2-4691-a350-009518995bce") {
                    id
                    title
                }
            }""".trimIndent()).execute()
            .path("movie")
            .valueIsNull()

        verify(movieService).getMovieById("2f251eae-09b2-4691-a350-009518995bce")
    }

    @Test
    fun `should return movie for movie id`() {
        whenever(movieService.getMovieById(any())).thenReturn(constructMovieDTO(
            id = "e60c085c-4ae6-4a62-96fe-4ffea2ab8b03"
        ))

        graphQlTester.document("""
            query {
                movie(id: "e60c085c-4ae6-4a62-96fe-4ffea2ab8b03") {
                    id
                    title
                    summary
                    releaseYear
                    rating
                    cast {
                        personId
                        firstName
                        lastName
                        sequence
                        characterName
                    }
                    crew {
                        personId
                        firstName
                        lastName
                        sequence
                        jobName
                    }
                }
            }""".trimIndent()).execute()
            .path("movie")
            .matchesJson("""
                {
                    "id": "e60c085c-4ae6-4a62-96fe-4ffea2ab8b03",
                    "title": "Test title",
                    "summary": "Test summary",
                    "releaseYear": 2026,
                    "rating": "5.0",
                    "cast": [
                        {
                            "personId": "d5171a9d-83a8-4695-9468-396b446b5d6d",
                            "firstName": "Peter",
                            "lastName": "P",
                            "sequence": 99,
                            "characterName": "Test Character"
                        }
                    ],
                    "crew": [
                       {
                            "personId": "3435c7bf-6087-4779-9646-23229e22182a",
                            "firstName": "Norman",
                            "lastName": "O",
                            "sequence": 99,
                            "jobName": "Director"
                        }
                    ]
                }""".trimIndent())

        verify(movieService).getMovieById("e60c085c-4ae6-4a62-96fe-4ffea2ab8b03")
    }

    @Test
    fun `should return movie title only for movie id`() {
        whenever(movieService.getMovieById(any())).thenReturn(constructMovieDTO(
            id = "f363b773-da4c-4a2d-8aa5-b77b66ffbf77"
        ))

        graphQlTester.document("""
            query {
                movie(id: "f363b773-da4c-4a2d-8aa5-b77b66ffbf77") {
                    title
                }
            }""".trimIndent()).execute()
            .path("movie")
            .matchesJson("""
                {
                    "title": "Test title"
                }""".trimIndent())

        verify(movieService).getMovieById("f363b773-da4c-4a2d-8aa5-b77b66ffbf77")
    }

    @Test
    fun `should return movie after add`() {
        whenever(movieService.addMovie(any())).thenReturn(constructMovieDTO(
            id = "093e0e4d-3f9e-4ceb-b116-60f1f89c3231",
            title = "Test title 2",
            summary = "Test summary 2",
            rating = "1.0",
            releaseYear = 2026))

        graphQlTester.document("""
            mutation {
                addMovie(movie: {
                    title: "Test title 2"
                    summary: "Test summary 2"
                    rating: "1.0"
                    releaseYear: 2026
                    releaseDate: "2026-04-01"
                    cast: [
                        {
                            personId: "d5171a9d-83a8-4695-9468-396b446b5d6d",
                            characterName: "Test Character"
                        }
                    ]
                    crew: [
                        {
                            personId: "3435c7bf-6087-4779-9646-23229e22182a",
                            jobName: "Director"
                        }
                    ]
                }) {
                    id
                    title
                    summary
                    rating
                    releaseYear
                    cast {
                        firstName
                        lastName
                        personId
                        characterName
                        sequence
                    }
                    crew {
                        firstName
                        jobName
                        lastName
                        personId
                        sequence
                    }
                }
            }""".trimIndent()).execute()
            .path("addMovie")
            .matchesJson("""
                {
                    "id": "093e0e4d-3f9e-4ceb-b116-60f1f89c3231",
                    "title": "Test title 2",
                    "summary": "Test summary 2",
                    "rating": "1.0",
                    "releaseYear": 2026,
                    "cast": [
                        {
                            "firstName": "Peter",
                            "lastName": "P",
                            "personId": "d5171a9d-83a8-4695-9468-396b446b5d6d",
                            "characterName": "Test Character",
                            "sequence": 99
                        }
                    ],
                    "crew": [
                        {
                            "firstName": "Norman",
                            "jobName": "Director",
                            "lastName": "O",
                            "personId": "3435c7bf-6087-4779-9646-23229e22182a",
                            "sequence": 99
                        }
                    ]
                }""".trimIndent())
    }

    private fun constructMovieDTO(id : String,
                                  title: String = "Test title",
                                  summary: String = "Test summary",
                                  rating: String = "5.0",
                                  releaseYear: Int = 2026) : MovieDTO {
        return MovieDTO(
            id = id,
            title = title,
            summary = summary,
            synopsis = "Test synopsis",
            rating = rating,
            releaseYear = releaseYear,
            releaseDate = "2020-10-02",
            cast = listOf(CastDTO("d5171a9d-83a8-4695-9468-396b446b5d6d",
                "Peter",
                "P",
                99,
                "Test Character",
                "e7619389-746a-49c4-aacd-16dff98d1c29")),
            crew = listOf(CrewDTO("3435c7bf-6087-4779-9646-23229e22182a",
                "Norman",
                "O",
                99,
                "Director",
                "3d4e0114-a34b-49c7-93e5-346affe298d7"))
        )
    }
}