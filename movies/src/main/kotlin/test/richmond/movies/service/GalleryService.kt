package test.richmond.movies.service

import io.grpc.StatusRuntimeException
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import test.richmond.filemgt.grpc.FileServiceGrpc
import test.richmond.filemgt.grpc.GetImagesRequest
import test.richmond.filemgt.grpc.Image
import test.richmond.filemgt.grpc.ImageOwner
import test.richmond.movies.model.GalleryMap
import test.richmond.movies.model.ImageType
import test.richmond.movies.model.dto.ImageDTO

@Service
class GalleryService(
    private val fileServiceStub: FileServiceGrpc.FileServiceBlockingStub
) {

    private val logger = LoggerFactory.getLogger(GalleryService::class.java)

    fun getImages(linkedIds: Set<String>) : GalleryMap {

        val allOwners = linkedIds.map { id -> buildImageOwner(id) }.toSet()

        try{
            logger.info("Getting images for ${allOwners.size} owners")
            val response = fileServiceStub.getImages(
                GetImagesRequest.newBuilder()
                    .addAllOwners(allOwners)
                    .build()
            )
            return GalleryMap(
                response.imagesList.stream()
                    .map { image -> mapToDTO(image) }
                    .toList()
            )
        } catch (e: StatusRuntimeException) {
            logger.error("gRPC status: ${e.status}")
            logger.error("Description: ${e.status.description}")
            logger.error("Cause: ${e.cause}")
            throw e
        } catch(e : Throwable) {
            logger.error("gRPC failure: $e")
            throw e
        }
    }

    fun buildImageOwner(linkedId: String) : ImageOwner {
        return ImageOwner.newBuilder()
            .setOwnerId(linkedId)
            .build()
    }

    fun mapToDTO(image: Image) : ImageDTO {
        return ImageDTO(
            id = image.imageId,
            linkedId = image.ownerId,
            url = image.fileUrl,
            imageType = getImageType(image.imageType)
        )
    }

    fun getImageType(imageType: test.richmond.filemgt.grpc.ImageType) : ImageType {
        return if(imageType == test.richmond.filemgt.grpc.ImageType.COVER){
            ImageType.COVER
        } else {
            ImageType.GALLERY
        }
    }
}