package test.richmond.filemgt.config

import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import test.richmond.filemgt.exception.FileNotFoundException

@RestControllerAdvice
class ControllerAdviceConfig {

    @ExceptionHandler(FileNotFoundException::class)
    fun handleFileNotFound(
        ex: FileNotFoundException
    ): ResponseEntity<ProblemDetail> {
        val problem = ProblemDetail.forStatusAndDetail(
            HttpStatus.NOT_FOUND,
            ex.message ?: "Image not found"
        )

        problem.title = "Image not found"

        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(problem)
    }
}