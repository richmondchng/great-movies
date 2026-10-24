package test.richmond.filemgt.service

import test.richmond.filemgt.data.entity.FileMetadataEntity

interface FileManager {

    fun getFileBytes(fileMetadata: FileMetadataEntity): ByteArray
}