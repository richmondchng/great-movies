package test.richmond.filemgt.util

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import test.richmond.filemgt.data.entity.FileMetadataEntity

@Component
class FileUrlUtil(
    @Value("\${app.files.download.host-name}")
    val hostname: String
) {

    fun constructGetImageUrl(fileMetadata: FileMetadataEntity) : String {
        return "$hostname/gallery/${fileMetadata.fileName}"
    }
}