package test.richmond.movies.controller

import java.time.Instant
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
        whenever(movieService.getMovies(any(), any())).thenReturn(
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

        verify(movieService).getMovies(0, 20)
    }

    @Test
    fun `should return list of movies`() {
        whenever(movieService.getMovies(any(), any()))
            .thenReturn(
                MoviesDTO(
                    movies = listOf(
                        constructMovieDTO(id = 1L, title = "Movie 1"),
                        constructMovieDTO(id = 2L, title = "Movie 2")
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
                            "id": "1",
                            "title": "Movie 1"
                        },
                        {
                            "id": "2",
                            "title": "Movie 2"
                        }
                    ],
                    "page": 1,
                    "pageSize": 10
                }""".trimIndent())

        verify(movieService).getMovies(1, 10)
    }

    @Test
    fun `should not return anything if movie id is invalid`() {
        whenever(movieService.getMovieById(any())).thenReturn(null)

        graphQlTester.document("""
            query {
                movie(id: 1) {
                    id
                    title
                }
            }""".trimIndent()).execute()
            .path("movie")
            .valueIsNull()

        verify(movieService).getMovieById(1L)
    }

    @Test
    fun `should return movie for movie id`() {
        whenever(movieService.getMovieById(any())).thenReturn(constructMovieDTO())

        graphQlTester.document("""
            query {
                movie(id: 1) {
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
                    "id": "1",
                    "title": "Test title",
                    "summary": "Test summary",
                    "releaseYear": 2026,
                    "rating": "5.0",
                    "cast": [
                        {
                            "personId": "1",
                            "firstName": "Peter",
                            "lastName": "P",
                            "sequence": 99,
                            "characterName": "Test Character"
                        }
                    ],
                    "crew": [
                       {
                            "personId": "2",
                            "firstName": "Norman",
                            "lastName": "O",
                            "sequence": 99,
                            "jobName": "Director"
                        }
                    ]
                }""".trimIndent())

        verify(movieService).getMovieById(1L)
    }

    @Test
    fun `should return movie title only for movie id`() {
        whenever(movieService.getMovieById(any())).thenReturn(constructMovieDTO())

        graphQlTester.document("""
            query {
                movie(id: 1) {
                    title
                }
            }""".trimIndent()).execute()
            .path("movie")
            .matchesJson("""
                {
                    "title": "Test title"
                }""".trimIndent())

        verify(movieService).getMovieById(1L)
    }

    @Test
    fun `should return movie after add`() {
        whenever(movieService.addMovie(any())).thenReturn(constructMovieDTO(
            id = 2L,
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
                    cast: [
                        {
                            personId: 1,
                            characterName: "Test Character"
                        }
                    ]
                    crew: [
                        {
                            personId: 2,
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
                    "id": "2",
                    "title": "Test title 2",
                    "summary": "Test summary 2",
                    "rating": "1.0",
                    "releaseYear": 2026,
                    "cast": [
                        {
                            "firstName": "Peter",
                            "lastName": "P",
                            "personId": "1",
                            "characterName": "Test Character",
                            "sequence": 99
                        }
                    ],
                    "crew": [
                        {
                            "firstName": "Norman",
                            "jobName": "Director",
                            "lastName": "O",
                            "personId": "2",
                            "sequence": 99
                        }
                    ]
                }""".trimIndent())
    }

    private fun constructMovieDTO(id : Long = 1L,
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
            cast = listOf(CastDTO(1L, "Peter", "P", 99, "Test Character", 1L)),
            crew = listOf(CrewDTO(2L, "Norman", "O", 99, "Director", 2L))
        )
    }
}