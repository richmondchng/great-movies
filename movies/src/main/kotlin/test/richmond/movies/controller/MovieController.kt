package test.richmond.movies.controller

import org.slf4j.LoggerFactory
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller
import test.richmond.movies.model.dto.MovieDTO
import test.richmond.movies.model.dto.MovieInput
import test.richmond.movies.service.MovieService

@Controller
class MovieController(
    private var movieService : MovieService
) {

    private val logger = LoggerFactory.getLogger(MovieService::class.java)

    @QueryMapping(name = "movie")
    fun movie(@Argument id: Long) : MovieDTO? {
        logger.info("Getting movie by id $id")
        return movieService.findMovieById(id)
    }

    @MutationMapping(name = "addMovie")
    fun addMovie(@Argument movie: MovieInput) : MovieDTO? {
        logger.info("Adding a new movie: ${movie.title}")
        return movieService.addMovie(movie)
    }

}