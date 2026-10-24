package test.richmond.filemgt.service.impl

import java.nio.file.Path
import java.time.Instant
import java.util.UUID
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import test.richmond.filemgt.data.entity.FileMetadataEntity
import test.richmond.filemgt.model.FileCategory
import kotlin.io.path.absolutePathString

class LocalFileManagerTest {

    private lateinit var localFileManager: LocalFileManager

    @BeforeEach
    fun setUp() {
        val resourceUrl = requireNotNull(
            javaClass.classLoader.getResource("test-files")
        )
        val path = Path.of(resourceUrl.toURI())
        localFileManager = LocalFileManager(
            path.absolutePathString()
        )
    }

    @AfterEach
    fun tearDown() {}

    @Test
    fun `should return file bytes`() {

        val expectedBytes = requireNotNull(
            javaClass.classLoader.getResourceAsStream(
                "test-files/2026/10/84d98d683ad849f38d4705fd9984a0fe.jpg"
            )
        ).use { it.readBytes() }

        val fileMetadata : FileMetadataEntity = FileMetadataEntity(
            id = UUID.fromString("3228bcbe-1237-4e79-a28e-377cc1189d7e"),
            linkedId = UUID.fromString("84d98d68-3ad8-49f3-8d47-05fd9984a0fe"),
            category = FileCategory.COVER,
            originalFileName = "abc.jpg",
            mimeType = "image/jpeg",
            fileSize = 10,
            filePath = "2026/10",
            fileName = "84d98d683ad849f38d4705fd9984a0fe.jpg",
            createdAt = Instant.now(),
            createdBy = "SYSTEM"
        )

        val result = localFileManager.getFileBytes(fileMetadata)

        assertArrayEquals(expectedBytes, result)
    }

    @Test
    fun `invalid file path should throw exception`() {

        val expectedBytes = requireNotNull(
            javaClass.classLoader.getResourceAsStream(
                "test-files/2026/10/84d98d683ad849f38d4705fd9984a0fe.jpg"
            )
        ).use { it.readBytes() }

        val fileMetadata : FileMetadataEntity = FileMetadataEntity(
            id = UUID.fromString("3228bcbe-1237-4e79-a28e-377cc1189d7e"),
            linkedId = UUID.fromString("84d98d68-3ad8-49f3-8d47-05fd9984a0fe"),
            category = FileCategory.COVER,
            originalFileName = "abc.jpg",
            mimeType = "image/jpeg",
            fileSize = 10,
            filePath = "//2026/10",
            fileName = "84d98d683ad849f38d4705fd9984a0fe.jpg",
            createdAt = Instant.now(),
            createdBy = "SYSTEM"
        )

        assertThatThrownBy {
            localFileManager.getFileBytes(fileMetadata)
        }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("Invalid file path")
    }

}