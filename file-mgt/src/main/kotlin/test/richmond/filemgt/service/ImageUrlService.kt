package test.richmond.filemgt.service

import io.grpc.stub.StreamObserver
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

@GrpcService
class ImageUrlService(
    @Value("\${app.file.host-name}")
    val hostname: String,
    val fileMetadataRepository: FileMetadataRepository
) : FileServiceGrpc.FileServiceImplBase()  {

    override fun getImages(
        request: GetImagesRequest?,
        responseObserver: StreamObserver<GetImagesForOwnersResponse?>?
    ) {

        val linkedIdList: List<String> = request?.ownersList?.map{ owner -> owner.ownerId}?.toList() ?: listOf()

        if(linkedIdList.isNotEmpty()) {
            val images = fileMetadataRepository.findAllByLinkedIdIn(linkedIdList)

            val allImages = images.map { imageMetadata ->
                Image.newBuilder()
                    .setOwnerId(imageMetadata.linkedId.toString())
                    .setImageType(getImageType(imageMetadata.category))
                    .setImageId(imageMetadata.id.toString())
                    .setFileUrl(getImageUrl(imageMetadata))
                    .build()
            }.toList()

            val response = GetImagesForOwnersResponse.newBuilder()
                .addAllImages(allImages)
                .build()

            responseObserver?.onNext(response)
        }
        responseObserver?.onCompleted()
    }

    private fun getImageUrl(fileMetadata: FileMetadataEntity) : String {
        return "$hostname/gallery/${fileMetadata.fileName}"
    }

    private fun getImageType(category: FileCategory) : ImageType {
        if(category == FileCategory.COVER) {
            return ImageType.COVER
        } else {
            return ImageType.GALLERY
        }
    }
}