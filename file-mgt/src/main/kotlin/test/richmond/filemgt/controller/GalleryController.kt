package test.richmond.filemgt.controller

import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import test.richmond.filemgt.service.FileDownloadService

@RestController
@RequestMapping("/gallery")
class GalleryController(
    val fileDownloadService: FileDownloadService
) {

    @GetMapping("/{id}")
    fun getFile(@PathVariable id: String): ResponseEntity<ByteArray> {
        val image = fileDownloadService.getFileBytes(id)

        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(image.contentType))
            .contentLength(image.bytes.size.toLong())
            .body(image.bytes)
    }

}