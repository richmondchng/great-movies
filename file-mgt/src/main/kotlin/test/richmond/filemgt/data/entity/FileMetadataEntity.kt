package test.richmond.filemgt.data.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant
import java.util.UUID
import test.richmond.filemgt.model.FileCategory

@Entity
@Table(name = "file_metadata")
class FileMetadataEntity(

    @Id
    val id: UUID? = null,

    @Column(name = "linked_id", nullable = false)
    val linkedId: UUID,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "category")
    val category: FileCategory = FileCategory.COVER,

    @Column(name = "original_file_name", nullable = false, length = 100)
    val originalFileName: String,

    @Column(name = "mime_type", length = 50)
    val mimeType: String? = null,

    @Column(name = "file_size")
    val fileSize: Long? = null,

    @Column(name = "file_path", length = 255)
    val filePath: String? = null,

    @Column(name = "file_name", nullable = false, length = 100)
    val fileName: String,

    @Column(name = "created_at", nullable=false)
    val createdAt: Instant = Instant.now(),

    @Column(name="created_by", length=50)
    val createdBy: String? = null
)