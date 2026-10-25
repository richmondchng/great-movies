package test.richmond.filemgt.service

import io.grpc.stub.StreamObserver
import jakarta.persistence.Column
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import java.time.Instant
import java.util.UUID
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoInteractions
import org.mockito.kotlin.whenever
import test.richmond.filemgt.data.entity.FileMetadataEntity
import test.richmond.filemgt.data.respository.FileMetadataRepository
import test.richmond.filemgt.grpc.GetImagesForOwnersResponse
import test.richmond.filemgt.grpc.GetImagesRequest
import test.richmond.filemgt.grpc.Image
import test.richmond.filemgt.grpc.ImageOwner
import test.richmond.filemgt.grpc.ImageType
import test.richmond.filemgt.model.FileCategory
import test.richmond.filemgt.util.FileUrlUtil
import kotlin.test.Test

@ExtendWith(MockitoExtension::class)
class ImageUrlServiceTest {

    @Mock
    lateinit var fileMetadataRepository: FileMetadataRepository

    @Mock
    lateinit var fileUrlUtil: FileUrlUtil

    @Mock
    lateinit var responseObserver: StreamObserver<GetImagesForOwnersResponse?>

    private lateinit var imageUrlService: ImageUrlService

    @BeforeEach
    fun setUp() {
        imageUrlService = ImageUrlService(fileMetadataRepository, fileUrlUtil)
    }

    @AfterEach
    fun tearDown() {}

    @Test
    fun `should not query database if no linked Id` () {
        imageUrlService.getImages(GetImagesRequest.getDefaultInstance(), responseObserver)

        verifyNoInteractions(fileMetadataRepository)
    }

    @Test
    fun `should return images for requested linked Id`() {

        val linkedId1 = UUID.randomUUID()
        val linkedId2 = UUID.randomUUID()
        val request = GetImagesRequest.newBuilder()
            .addOwners(
                ImageOwner.newBuilder()
                    .setOwnerId(linkedId1.toString())
                    .build())
            .addOwners(
                ImageOwner.newBuilder()
                    .setOwnerId(linkedId2.toString())
                    .build())
            .build()

        val fileMetadataEntity1 : FileMetadataEntity = createFileMetadataEntity(UUID.randomUUID(), linkedId1, FileCategory.COVER, "file-a.jpg")
        val fileMetadataEntity2 : FileMetadataEntity = createFileMetadataEntity(UUID.randomUUID(), linkedId1, FileCategory.GALLERY, "file-b.jpg")
        val fileMetadataEntity3 : FileMetadataEntity = createFileMetadataEntity(UUID.randomUUID(), linkedId2, FileCategory.COVER, "file-c.jpg")

        whenever(fileMetadataRepository.findAllByLinkedIdIn(any())).thenReturn(listOf(fileMetadataEntity1, fileMetadataEntity2, fileMetadataEntity3))

        whenever(fileUrlUtil.constructGetImageUrl(any())).thenReturn(
            "http://localhost/gallery/file-a.jpg",
            "http://localhost/gallery/file-b.jpg",
            "http://localhost/gallery/file-c.jpg")

        imageUrlService.getImages(request, responseObserver);

        val linkedIdsCaptor = argumentCaptor<List<UUID>>()
        verify(fileMetadataRepository).findAllByLinkedIdIn(linkedIdsCaptor.capture())
        assertThat(linkedIdsCaptor.firstValue).containsExactlyInAnyOrder(linkedId1, linkedId2)

        val getImagesForOwnersResponseCaptor = argumentCaptor<GetImagesForOwnersResponse>()
        verify( responseObserver, times(1)).onNext(getImagesForOwnersResponseCaptor.capture())
        val iterator = getImagesForOwnersResponseCaptor.firstValue.imagesList
            .stream()
            .sorted(Comparator.comparing { v -> v.fileUrl  })
            .iterator()

        val result1: Image = iterator.next()
        assertThat(result1.ownerId).isEqualTo(linkedId1.toString())
        assertThat(result1.imageType).isEqualTo(ImageType.COVER)
        assertThat(result1.imageId).isEqualTo(fileMetadataEntity1.id.toString())
        assertThat(result1.fileUrl).isEqualTo("http://localhost/gallery/file-a.jpg")

        val result2: Image = iterator.next()
        assertThat(result2.ownerId).isEqualTo(linkedId1.toString())
        assertThat(result2.imageType).isEqualTo(ImageType.GALLERY)
        assertThat(result2.imageId).isEqualTo(fileMetadataEntity2.id.toString())
        assertThat(result2.fileUrl).isEqualTo("http://localhost/gallery/file-b.jpg")

        val result3: Image = iterator.next()
        assertThat(result3.ownerId).isEqualTo(linkedId2.toString())
        assertThat(result3.imageType).isEqualTo(ImageType.COVER)
        assertThat(result3.imageId).isEqualTo(fileMetadataEntity3.id.toString())
        assertThat(result3.fileUrl).isEqualTo("http://localhost/gallery/file-c.jpg")

        verify( responseObserver, times(1)).onCompleted()
    }

    fun createFileMetadataEntity(id: UUID,
                                 linkedId: UUID,
                                 category: FileCategory = FileCategory.COVER,
                                 fileName: String) : FileMetadataEntity {
        return FileMetadataEntity(
            id = id,
            linkedId = linkedId,
            category = category,
            originalFileName = "",
            mimeType = "",
            fileSize = 10,
            filePath = "",
            fileName = fileName,
            createdAt = Instant.now(),
            createdBy = "TEST"
        )
    }
}