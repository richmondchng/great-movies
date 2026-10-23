package test.richmond.filemgt.data.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.time.Instant
import java.time.OffsetDateTime

@Entity
@Table(name = "file_upload_items")
class FileUploadItemEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "file_upload_id", nullable = false)
    val fileUpload: FileUploadEntity,

    @Column(name = "file_name", nullable = false, length = 255)
    val fileName: String,

    @Column(name = "file_path", length = 1000)
    val filePath: String? = null,

    @Column(name = "content_type", length = 100)
    val contentType: String? = null,

    @Column(name = "file_size")
    val fileSize: Long? = null,

    @Column(name = "created_at", nullable = false)
    val createdAt: Instant = Instant.now()
)