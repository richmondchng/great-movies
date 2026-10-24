package test.richmond.filemgt.service

import java.time.Instant
import java.util.UUID
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoInteractions
import org.mockito.kotlin.whenever
import test.richmond.filemgt.data.entity.FileMetadataEntity
import test.richmond.filemgt.data.respository.FileMetadataRepository
import test.richmond.filemgt.exception.FileNotFoundException
import test.richmond.filemgt.model.FileCategory
import test.richmond.filemgt.model.FileDownloadDTO

@ExtendWith(MockitoExtension::class)
class FileDownloadServiceTest{
    @Mock
    lateinit var fileMetadataRepository: FileMetadataRepository
    @Mock
    lateinit var fileManager: FileManager

    private lateinit var fileDownloadService: FileDownloadService

    @BeforeEach
    fun setUp() {
        fileDownloadService = FileDownloadService(fileMetadataRepository, fileManager)
    }

    @AfterEach
    fun tearDown() {}

    @Test
    fun `should throw exception if file name does not exist`() {
        whenever(fileMetadataRepository.findByFileName(any())).thenReturn(null)

        assertThatThrownBy{ fileDownloadService.getFileBytes("84d98d683ad849f38d4705fd9984a0fe.jpg") }
            .isInstanceOf(FileNotFoundException::class.java)
            .hasMessage("File not found: 84d98d683ad849f38d4705fd9984a0fe.jpg")

        verifyNoInteractions(fileManager)
    }

    @Test
    fun `should return populated file download DTO`() {
        val fileMetadata : FileMetadataEntity = FileMetadataEntity(
            id = UUID.fromString("3228bcbe-1237-4e79-a28e-377cc1189d7e"),
            linkedId = UUID.fromString("84d98d68-3ad8-49f3-8d47-05fd9984a0fe"),
            category = FileCategory.COVER,
            originalFileName = "abc.jpg",
            mimeType = "image/jpeg",
            fileSize = 10,
            filePath = "/test-files/files/2026/10",
            fileName = "84d98d683ad849f38d4705fd9984a0fe.jpg",
            createdAt = Instant.now(),
            createdBy = "SYSTEM"
        )
        whenever(fileMetadataRepository.findByFileName(any())).thenReturn(fileMetadata)

        val expectedBytes = "hello world".toByteArray()
        whenever(fileManager.getFileBytes(any())).thenReturn(expectedBytes)

        val result: FileDownloadDTO = fileDownloadService.getFileBytes(fileMetadata.fileName)

        assertThat(result).isNotNull()
        assertThat(result.id).isEqualTo(fileMetadata.id.toString())
        assertThat(result.mimeType).isEqualTo(fileMetadata.mimeType)
        assertThat(result.bytes).isEqualTo(expectedBytes)

        verify(fileMetadataRepository, times(1)).findByFileName(fileMetadata.fileName)
        verify(fileManager).getFileBytes(fileMetadata)
    }

}