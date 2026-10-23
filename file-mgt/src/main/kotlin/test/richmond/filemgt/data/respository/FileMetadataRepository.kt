package test.richmond.filemgt.data.respository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import test.richmond.filemgt.data.entity.FileMetadataEntity

interface FileMetadataRepository: JpaRepository<FileMetadataEntity, Long> {

    @Query("SELECT f FROM FileMetadataEntity f WHERE f.linkedId IN :linkedIds")
    fun findAllByLinkedIdIn(@Param("linkedIds") linkedIds: List<String>): List<FileMetadataEntity>
}