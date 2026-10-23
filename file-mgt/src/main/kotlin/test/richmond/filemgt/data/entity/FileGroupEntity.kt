package test.richmond.filemgt.data.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import java.time.Instant
import org.hibernate.annotations.CreationTimestamp
import test.richmond.filemgt.model.ArtifactType

@Entity
@Table(name = "file_upload")
class FileGroupEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "artifact_type")
    val artifactType: ArtifactType = ArtifactType.MOVIE,

    @Column(nullable = false, name = "artifact_id")
    val artifactId: Long,

    @CreationTimestamp
    @Column(name = "created_at")
    val createdAt: Instant = Instant.now(),

    @Column(nullable = false, name = "created_by")
    var createdBy: String?,

    @OneToMany(
        mappedBy = "fileUpload",
        cascade = [CascadeType.ALL],
        orphanRemoval = true
    )
    val items: MutableList<FileMetadataEntity> = mutableListOf()

)