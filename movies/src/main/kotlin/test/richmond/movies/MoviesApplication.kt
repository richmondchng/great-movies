package test.richmond.movies

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.grpc.client.ImportGrpcClients
import test.richmond.filemgt.grpc.FileServiceGrpc

@SpringBootApplication
@ImportGrpcClients(target = "file-service", types = [FileServiceGrpc.FileServiceBlockingStub::class])
class MoviesApplication

fun main(args: Array<String>) {
	runApplication<MoviesApplication>(*args)
}
