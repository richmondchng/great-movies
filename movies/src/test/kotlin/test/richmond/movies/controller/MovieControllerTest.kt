package test.richmond.movies.controller

import java.time.Instant
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
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
import test.richmond.movies.service.MovieService

@GraphQlTest(MovieController::class)
@Import(GraphQLConfig::class)
class MovieControllerTest @Autowired constructor(
    private val graphQlTester: GraphQlTester
) {

    @MockitoBean
    lateinit var movieService: MovieService

    @Test
    fun `should return movie for movie id`() {
        whenever(movieService.findMovieById(any())).thenReturn(constructMovieDTO())

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
                            "lastName": "Parker",
                            "sequence": 1,
                            "characterName": "Spiderman"
                        }
                    ],
                    "crew": [
                       {
                            "personId": "2",
                            "firstName": "Norman",
                            "lastName": "Osborn",
                            "sequence": 1,
                            "jobName": "Director"
                        }
                    ]
                }""".trimIndent())
    }

    @Test
    fun `should return movie title only for movie id`() {
        whenever(movieService.findMovieById(any())).thenReturn(constructMovieDTO())

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
                }) {
                    id
                    title
                    summary
                    rating
                    releaseYear
                }
            }""".trimIndent()).execute()
            .path("addMovie")
            .matchesJson("""
                {
                    "id": "2",
                    "title": "Test title 2",
                    "summary": "Test summary 2",
                    "rating": "1.0",
                    "releaseYear": 2026
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
            Instant.now(),
            cast = listOf(CastDTO(1L, "Peter", "Parker", 1, "Spiderman", 1L)),
            crew = listOf(CrewDTO(2L, "Norman", "Osborn", 1, "Director", 2L))
        )
    }
}