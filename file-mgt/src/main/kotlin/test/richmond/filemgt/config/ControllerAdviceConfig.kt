package test.richmond.filemgt.config

import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import test.richmond.filemgt.exception.FileNotFoundException
import test.richmond.filemgt.exception.FileTypeNotRecognizedException

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


    @ExceptionHandler(FileTypeNotRecognizedException::class)
    fun handleFileNotRecognizedUploadError(
        ex: FileTypeNotRecognizedException
    ): ResponseEntity<ProblemDetail> {
        val problem = ProblemDetail.forStatusAndDetail(
            HttpStatus.BAD_REQUEST,
            ex.message ?: "File type not recognized"
        )

        problem.title = "File type not recognized"

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(problem)
    }
}