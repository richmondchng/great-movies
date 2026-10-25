package test.richmond.filemgt.controller

import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import test.richmond.filemgt.model.FileCategory
import test.richmond.filemgt.model.FileUploadDTO
import test.richmond.filemgt.service.FileUploadService

@RestController
class FileUploadController(
    val fileUploadService: FileUploadService
) {

    @PostMapping(
        value = ["/api/v1/upload"],
        consumes = [MediaType.MULTIPART_FORM_DATA_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun uploadFile(
        @RequestParam("file") file: MultipartFile,
        @RequestParam("id") id: String,
        @RequestParam("imageType") imageType: String
    ): ResponseEntity<FileUploadDTO> {

        var fileCategory = FileCategory.COVER
        if (FileCategory.GALLERY.name.equals(imageType)) {
            fileCategory = FileCategory.GALLERY
        }
        val fileUpload = fileUploadService.uploadFile(file, id, fileCategory)
        // response
        return ResponseEntity.ok(fileUpload)
    }
}