package test.richmond.movies.service

import java.time.Instant
import java.time.LocalDate
import java.util.Optional
import org.springframework.stereotype.Service
import test.richmond.movies.model.dto.MovieDTO

@Service
class MovieService {

    fun findMovieById(id: Long): MovieDTO? {
        return MovieDTO(1L, "Test", "Test", "Test", LocalDate.now(), Instant.now())
    }

}
