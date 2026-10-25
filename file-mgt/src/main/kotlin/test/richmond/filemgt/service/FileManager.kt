package test.richmond.filemgt.service

import org.springframework.web.multipart.MultipartFile
import test.richmond.filemgt.data.entity.FileMetadataEntity
import test.richmond.filemgt.model.FileDetailsDTO

interface FileManager {

    fun getFileBytes(fileMetadata: FileMetadataEntity): ByteArray

    fun copyFile(multipartFile : MultipartFile, destinationFileName: String) : FileDetailsDTO
}