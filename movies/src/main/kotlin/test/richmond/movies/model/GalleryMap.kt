package test.richmond.movies.model

import test.richmond.movies.model.dto.ImageDTO

class GalleryMap(
    images : List<ImageDTO>
) {
    private val coverImages = mutableMapOf<String, ImageDTO>()
    private val galleryImages = mutableMapOf<String, MutableList<ImageDTO>>()

    init {

        images.forEach { image ->
            if(image.imageType == ImageType.GALLERY) {
                galleryImages
                    .getOrPut(image.linkedId) { mutableListOf() }
                    .add(image)
            } else {
                if(coverImages[image.linkedId] == null) {
                    coverImages[image.linkedId] = image
                }
            }
        }
    }

    fun getCover(id: String) : ImageDTO? {
        return coverImages[id]
    }

    fun getImages(id: String) : List<ImageDTO> {
        return galleryImages[id]?.toList() ?: emptyList()
    }
}