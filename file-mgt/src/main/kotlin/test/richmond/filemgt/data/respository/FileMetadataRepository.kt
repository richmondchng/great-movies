package test.richmond.filemgt.data.respository

import org.springframework.data.jpa.repository.JpaRepository
import test.richmond.filemgt.data.entity.FileMetadataEntity

interface FileMetadataRepository: JpaRepository<FileMetadataEntity, Long> {
}