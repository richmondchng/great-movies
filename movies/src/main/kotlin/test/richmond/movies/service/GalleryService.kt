package test.richmond.movies.service

import io.grpc.StatusRuntimeException
import java.util.stream.Collectors
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import test.richmond.filemgt.grpc.FileServiceGrpc
import test.richmond.filemgt.grpc.GetImagesRequest
import test.richmond.filemgt.grpc.Image
import test.richmond.filemgt.grpc.ImageOwner
import test.richmond.movies.model.dto.ImageDTO

@Service
class GalleryService(
    private val fileServiceStub: FileServiceGrpc.FileServiceBlockingStub
) {

    private val logger = LoggerFactory.getLogger(GalleryService::class.java)

    fun getImages(linkedIds: Set<String>) : Map<String, List<ImageDTO>> {

        val allOwners = linkedIds.map { id -> buildImageOwner(id) }.toList();

        try{
            logger.info("Getting images for ${allOwners.size} owners")
            val response = fileServiceStub.getImages(
                GetImagesRequest.newBuilder()
                    .addAllOwners(allOwners)
                    .build()
            )
            logger.info("Request end")
            return response.imagesList.stream()
                .map { image -> mapToDTO(image) }
                .collect(Collectors.groupingBy { image: ImageDTO -> image.linkedId})
        } catch (ex: StatusRuntimeException) {
            println("gRPC status: ${ex.status}")
            println("Description: ${ex.status.description}")
            println("Cause: ${ex.cause}")
            throw ex
        } catch(ex : Throwable) {
            println("gRPC failure: ${ex}")
            throw ex
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
            imageUrl = image.fileUrl
        )
    }
}