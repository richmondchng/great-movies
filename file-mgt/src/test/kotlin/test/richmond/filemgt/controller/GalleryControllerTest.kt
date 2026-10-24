package test.richmond.filemgt.controller

import java.util.UUID
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import test.richmond.filemgt.exception.FileNotFoundException
import test.richmond.filemgt.model.FileDownloadDTO
import test.richmond.filemgt.service.FileDownloadService

@WebMvcTest(controllers = [GalleryController::class])
class GalleryControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockitoBean
    private lateinit var fileDownloadService: FileDownloadService

    @Test
    fun `should return byte array if file exists`() {
        val bytes = "this is a test".toByteArray()
        whenever(fileDownloadService.getFileBytes(any())).thenReturn(FileDownloadDTO(
            bytes = bytes,
            mimeType = "image/jpeg",
            contentType = "image/jpeg",
            id = UUID.randomUUID().toString(),
        ))

        mockMvc.perform(get("/gallery/abc.jpg"))
            .andExpect(status().isOk)
            .andExpect(content().contentType("image/jpeg"))
            .andExpect(content().bytes(bytes))

        verify(fileDownloadService).getFileBytes("abc.jpg")
    }

    @Test
    fun `should return 404 if file does not exists`() {
        val bytes = "this is a test".toByteArray()
        whenever(fileDownloadService.getFileBytes(any())).thenThrow(FileNotFoundException("abc.jpg"))

        mockMvc.perform(get("/gallery/abc.jpg"))
            .andExpect(status().is4xxClientError)

        verify(fileDownloadService).getFileBytes("abc.jpg")
    }

}