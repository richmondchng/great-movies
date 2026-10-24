package test.richmond.movies.model

import org.assertj.core.api.Assertions.assertThat
import test.richmond.movies.model.dto.ImageDTO
import kotlin.String
import kotlin.test.Test

class GalleryMapTest {

    @Test
    fun `should only get one cover image per linkedId`() {
        val galleryMap = GalleryMap(listOf(
            imageDTO("1", "11", ImageType.COVER),
            imageDTO("2", "11", ImageType.COVER),
            imageDTO("3", "12", ImageType.COVER),
            imageDTO("4", "12", ImageType.GALLERY),
            imageDTO("5", "12", ImageType.GALLERY),
            imageDTO("6", "11", ImageType.GALLERY)
        ))

        val cover1 = galleryMap.getCover("12")
        assertThat(cover1).isNotNull
        assertThat(cover1?.id).isEqualTo("3")
    }

    @Test
    fun `should only get first cover image if multiple cover images per linkedId`() {
        val galleryMap = GalleryMap(listOf(
            imageDTO("1", "11", ImageType.COVER),
            imageDTO("2", "11", ImageType.COVER),
            imageDTO("3", "12", ImageType.COVER),
            imageDTO("4", "12", ImageType.GALLERY),
            imageDTO("5", "12", ImageType.GALLERY),
            imageDTO("6", "11", ImageType.GALLERY)
        ))

        val cover1 = galleryMap.getCover("11")
        assertThat(cover1).isNotNull
        assertThat(cover1?.id).isEqualTo("1")
    }

    @Test
    fun `should not get cover image if linkedId has no record`() {
        val galleryMap = GalleryMap(listOf(
            imageDTO("1", "11", ImageType.COVER),
            imageDTO("2", "11", ImageType.COVER),
            imageDTO("3", "12", ImageType.COVER),
            imageDTO("4", "12", ImageType.GALLERY),
            imageDTO("5", "12", ImageType.GALLERY),
            imageDTO("6", "11", ImageType.GALLERY)
        ))

        val cover1 = galleryMap.getCover("13")
        assertThat(cover1).isNull()
    }

    @Test
    fun `should images if multiple there are images for linkedId`() {
        val galleryMap = GalleryMap(listOf(
            imageDTO("1", "11", ImageType.COVER),
            imageDTO("2", "11", ImageType.COVER),
            imageDTO("3", "12", ImageType.COVER),
            imageDTO("4", "12", ImageType.GALLERY),
            imageDTO("5", "12", ImageType.GALLERY),
            imageDTO("6", "11", ImageType.GALLERY)
        ))

        val images = galleryMap.getImages("12")
        assertThat(images).hasSize(2)
        assertThat(images.stream().map { v -> v.id }.toList()).containsExactlyInAnyOrder("4", "5")
    }

    @Test
    fun `should not get images if linkedId has no record`() {
        val galleryMap = GalleryMap(listOf(
            imageDTO("1", "11", ImageType.COVER),
            imageDTO("2", "11", ImageType.COVER),
            imageDTO("3", "12", ImageType.COVER),
            imageDTO("4", "12", ImageType.GALLERY),
            imageDTO("5", "12", ImageType.GALLERY),
            imageDTO("6", "11", ImageType.GALLERY)
        ))

        val images = galleryMap.getImages("13")
        assertThat(images).isEmpty()
    }

    private fun imageDTO(id: String, linkedId: String, imageType: ImageType) : ImageDTO {
        return ImageDTO(
            id = id,
            linkedId = linkedId,
            imageType = imageType,
        )
    }
}