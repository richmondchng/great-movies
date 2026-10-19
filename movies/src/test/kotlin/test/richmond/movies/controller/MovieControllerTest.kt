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
        whenever(movieService.findMovieById(any())).thenReturn(
            MovieDTO(
                1L,
                "Test title",
                "Test summary",
                "Test synopsis",
                "5.0",
                2026,
                Instant.now()
            )
        )

        graphQlTester.document("""
            query {
                movie(id: 1) {
                    id
                    title
                    summary
                    synopsis
                    releaseYear
                    rating
                }
            }""".trimIndent()).execute()
            .path("movie")
            .matchesJson("""
                {
                    "id": "1",
                    "title": "Test title",
                    "summary": "Test summary",
                    "synopsis": "Test synopsis",
                    "releaseYear": 2026,
                    "rating": "5.0"
                }""".trimIndent())
    }

    @Test
    fun `should return movie title only for movie id`() {
        whenever(movieService.findMovieById(any())).thenReturn(
            MovieDTO(
                1L,
                "Test title",
                "Test summary",
                "Test synopsis",
                "5.0",
                2026,
                Instant.now()
            )
        )

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
}