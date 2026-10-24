package test.richmond.filemgt.service

import org.springframework.stereotype.Service
import test.richmond.filemgt.data.entity.FileMetadataEntity
import test.richmond.filemgt.data.respository.FileMetadataRepository
import test.richmond.filemgt.exception.FileNotFoundException
import test.richmond.filemgt.model.FileDownloadDTO
import kotlin.String

@Service
class FileDownloadService(
    val fileMetadataRepository: FileMetadataRepository,
    val fileManager: FileManager
) {

    fun getFileBytes(id: String) : FileDownloadDTO {
        val fileMetadataEntity = fileMetadataRepository.findByFileName(id) ?: throw FileNotFoundException(id)
        val bytes = fileManager.getFileBytes(fileMetadataEntity)
        return mapToFileDownloadDTO(fileMetadataEntity, bytes)
    }

    private fun mapToFileDownloadDTO(fileMetadataEntity: FileMetadataEntity, bytes: ByteArray) : FileDownloadDTO {
        return FileDownloadDTO(
            id = fileMetadataEntity.id.toString(),
            bytes = bytes,
            mimeType = fileMetadataEntity.mimeType ?: "",
            contentType = fileMetadataEntity.mimeType ?: ""
        )
    }
}