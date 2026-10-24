package test.richmond.filemgt.service

import org.springframework.stereotype.Service
import test.richmond.filemgt.model.FileDownloadDTO

@Service
class FileDownloadService {

    fun getFileBytes(id: String) : FileDownloadDTO {
        throw NotImplementedError()
    }
}