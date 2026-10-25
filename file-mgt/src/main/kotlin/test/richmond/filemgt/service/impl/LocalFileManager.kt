package test.richmond.filemgt.service.impl

import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.time.LocalDate
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import test.richmond.filemgt.data.entity.FileMetadataEntity
import test.richmond.filemgt.exception.UnableToSaveFileException
import test.richmond.filemgt.model.FileDetailsDTO
import test.richmond.filemgt.service.FileManager

@Primary
@Component
class LocalFileManager(
    @Value("\${app.files.volume.root}")
    filePath: String
): FileManager {

    private val logger = LoggerFactory.getLogger(LocalFileManager::class.java)

    private val storagePath: Path = Paths.get(filePath)
        .toAbsolutePath()
        .normalize()

    init {
        logger.info("Initializing local filesystem ${storagePath.toAbsolutePath()}")
        Files.createDirectories(storagePath)
    }

    override fun getFileBytes(fileMetadata: FileMetadataEntity): ByteArray {
        var relativePath = "${fileMetadata.filePath}${File.separator}${fileMetadata.fileName}"
        if(relativePath.startsWith("/")) {
            relativePath = relativePath.substring(1)
        }
        val path = storagePath.resolve(relativePath).normalize()

        require(path.startsWith(storagePath)) {
            "Invalid file path"
        }
        return Files.readAllBytes(path)
    }

    override fun copyFile(
        multipartFile: MultipartFile,
        destinationFileName: String
    ): FileDetailsDTO {

        val destinationFilePath = generateUploadPath(LocalDate.now())
        try {
            val relativePath = "${destinationFilePath}${File.separator}${destinationFileName}"
            val path = storagePath.resolve(relativePath).normalize()
            val newFile: File = File(path.toAbsolutePath().toString())
            multipartFile.transferTo(newFile)
            logger.info("File saved to {}", newFile.absolutePath)
            // save to repository
            return FileDetailsDTO(
                fileName = destinationFileName,
                filePath = destinationFilePath
            )
        } catch (e: IOException) {
            throw UnableToSaveFileException(e)
        }
    }

    private fun generateUploadPath(date: LocalDate): String {
        return "%04d%s%02d".format(date.year, File.separator, date.monthValue)
    }
}