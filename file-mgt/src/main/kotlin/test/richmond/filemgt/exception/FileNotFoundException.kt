package test.richmond.filemgt.exception

class FileNotFoundException(imageId: String): RuntimeException("File not found: $imageId") {
}