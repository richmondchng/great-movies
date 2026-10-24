package test.richmond.movies.service

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import test.richmond.filemgt.grpc.FileServiceGrpc
import test.richmond.filemgt.grpc.GetImagesForOwnersResponse
import test.richmond.filemgt.grpc.GetImagesRequest
import test.richmond.movies.model.GalleryMap

@ExtendWith(MockitoExtension::class)
class GalleryServiceTest {

    @Mock
    lateinit var fileServiceBlockingStub: FileServiceGrpc.FileServiceBlockingStub

    private lateinit var galleryService: GalleryService

    @BeforeEach
    fun setUp() {
        galleryService = GalleryService(fileServiceBlockingStub)
    }

    @AfterEach
    fun tearDown() {}

    @Test
    fun `should get images for provided ids`() {

        whenever(fileServiceBlockingStub.getImages(any())).thenReturn(GetImagesForOwnersResponse.newBuilder().build())

        val results: GalleryMap = galleryService.getImages(setOf("1", "2"))

        val argumentCaptor = argumentCaptor<GetImagesRequest>()
        verify(fileServiceBlockingStub).getImages(argumentCaptor.capture())

        val getImageRequest = argumentCaptor.firstValue
        assertThat(getImageRequest.ownersList.stream().map { v -> v.ownerId }.toList()).containsExactlyInAnyOrder("1", "2")
    }

}