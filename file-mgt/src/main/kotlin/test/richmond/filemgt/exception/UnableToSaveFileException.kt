package test.richmond.filemgt.exception

class UnableToSaveFileException(
    cause: Throwable? = null
): RuntimeException("Unable to save file", cause) {
}