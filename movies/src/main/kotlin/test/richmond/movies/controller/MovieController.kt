package test.richmond.movies.controller

import lombok.extern.slf4j.Slf4j
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller
import test.richmond.movies.model.dto.MovieDTO
import test.richmond.movies.service.MovieService

@Slf4j
@Controller
class MovieController(
    private var movieService : MovieService
) {


    @QueryMapping(name = "movie")
    fun movie(@Argument id: Long) : MovieDTO? {
        return movieService.findMovieById(id)
    }

}