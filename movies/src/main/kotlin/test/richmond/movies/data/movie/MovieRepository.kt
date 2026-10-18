package test.richmond.movies.data.movie

import org.springframework.data.jpa.repository.JpaRepository

interface MovieRepository: JpaRepository<MovieEntity, Long> {
}