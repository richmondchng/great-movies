package test.richmond.filemgt.service

import io.grpc.stub.StreamObserver
import java.util.UUID
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.grpc.server.service.GrpcService
import test.richmond.filemgt.data.entity.FileMetadataEntity
import test.richmond.filemgt.data.respository.FileMetadataRepository
import test.richmond.filemgt.grpc.FileServiceGrpc
import test.richmond.filemgt.grpc.GetImagesForOwnersResponse
import test.richmond.filemgt.grpc.GetImagesRequest
import test.richmond.filemgt.grpc.Image
import test.richmond.filemgt.grpc.ImageType
import test.richmond.filemgt.model.FileCategory
import test.richmond.filemgt.util.FileUrlUtil

@GrpcService
class ImageUrlService(
    val fileMetadataRepository: FileMetadataRepository,
    val fileUrlUtil: FileUrlUtil
) : FileServiceGrpc.FileServiceImplBase()  {

    private val logger = LoggerFactory.getLogger(ImageUrlService::class.java)

    override fun getImages(
        request: GetImagesRequest?,
        responseObserver: StreamObserver<GetImagesForOwnersResponse?>?
    ) {
        val linkedIdList: List<UUID> = request?.ownersList?.map{ owner -> UUID.fromString(owner.ownerId)}?.toList() ?: listOf()

        logger.info("Get images for ${linkedIdList.size} owners $linkedIdList")

        try {
            val allImages: List<Image>
            if (linkedIdList.isNotEmpty()) {
                val images = fileMetadataRepository.findAllByLinkedIdIn(linkedIdList)

                allImages = images.map { imageMetadata ->
                    Image.newBuilder()
                        .setOwnerId(imageMetadata.linkedId.toString())
                        .setImageType(getImageType(imageMetadata.category))
                        .setImageId(imageMetadata.id.toString())
                        .setFileUrl(getImageUrl(imageMetadata))
                        .build()
                }.toList()
            } else {
                allImages = emptyList()
            }
            logger.info("Returning ${allImages.size} images")
            val response = GetImagesForOwnersResponse.newBuilder()
                .addAllImages(allImages)
                .build()

            responseObserver?.onNext(response)
            responseObserver?.onCompleted()
        } catch (e: Exception) {
            logger.error("Error during getting images", e)
        }
    }

    private fun getImageUrl(fileMetadata: FileMetadataEntity) : String {
        return fileUrlUtil.constructGetImageUrl(fileMetadata)
    }

    private fun getImageType(category: FileCategory) : ImageType {
        if(category == FileCategory.COVER) {
            return ImageType.COVER
        } else {
            return ImageType.GALLERY
        }
    }
}