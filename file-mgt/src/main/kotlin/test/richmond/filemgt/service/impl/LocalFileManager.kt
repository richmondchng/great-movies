package test.richmond.filemgt.service.impl

import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Component
import test.richmond.filemgt.data.entity.FileMetadataEntity
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
}