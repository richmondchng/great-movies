package test.richmond.filemgt.data.respository

import org.springframework.data.jpa.repository.JpaRepository
import test.richmond.filemgt.data.entity.FileUploadItemEntity

interface FileUploadRepository: JpaRepository<FileUploadItemEntity, Long> {
}