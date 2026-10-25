package test.richmond.filemgt.service

import java.time.Instant
import java.util.UUID
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import test.richmond.filemgt.data.entity.FileMetadataEntity
import test.richmond.filemgt.data.respository.FileMetadataRepository
import test.richmond.filemgt.exception.FileIsEmptyException
import test.richmond.filemgt.exception.FileTypeNotRecognizedException
import test.richmond.filemgt.model.FileCategory
import test.richmond.filemgt.model.FileUploadDTO
import test.richmond.filemgt.util.FileUrlUtil

@Service
class FileUploadService(
    @Value("\${app.files.upload.white-listed}")
    val whiteListed: Set<String>,
    val fileManager: FileManager,
    val fileMetadataRepository: FileMetadataRepository,
    val fileUrlUtil: FileUrlUtil
) {

    private val logger = LoggerFactory.getLogger(FileUploadService::class.java)

    init {
        logger.info("#### White listed document types {}", whiteListed)
    }

    fun uploadFile(multipartFile : MultipartFile, id: String, imageType: FileCategory): FileUploadDTO{

        if (multipartFile.isEmpty) {
            throw FileIsEmptyException()
        }
        logger.debug("Get multipartFile {} {}", multipartFile.originalFilename, multipartFile.contentType)

        val linkedId = UUID.fromString(id)

        val extension: String = getFileExtension(multipartFile.originalFilename)
        if (!isFileTypeWhiteListed(extension)) {
            throw FileTypeNotRecognizedException()
        }

        val uploadId = UUID.randomUUID();
        val fileName: String = generateFileName(uploadId.toString(), extension)
        val fileDetails = fileManager.copyFile(multipartFile, fileName)
        val metaData = FileMetadataEntity(
            id = uploadId,
            originalFileName = multipartFile.originalFilename ?: multipartFile.name,
            filePath = fileDetails.filePath,
            fileName = fileDetails.fileName,
            fileSize = multipartFile.size,
            category = imageType,
            mimeType = multipartFile.contentType,
            createdBy = "SYSTEM",
            linkedId = linkedId,
            createdAt = Instant.now()
        )
        fileMetadataRepository.save(metaData)
        return FileUploadDTO(
            id = metaData.id.toString(),
            linkedId = metaData.linkedId.toString(),
            url = fileUrlUtil.constructGetImageUrl(metaData),
            imageType = metaData.category.toString()
        )
    }

    private fun getFileExtension(fileName: String?): String {
        val period : Int? = fileName?.lastIndexOf(".")
        if (period == null || period < 0) {
            throw FileTypeNotRecognizedException()
        }
        return fileName.substring(period + 1)
    }

    private fun isFileTypeWhiteListed(extension: String): Boolean {
        return whiteListed.contains(extension)
    }

    private fun generateFileName(id: String, extension: String): String {
        val fileName = id.replace("-", "")
        return "${fileName}.${extension}"
    }
}