package org.kzk.minio;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.messages.Bucket;
import org.kzk.minio.ex.MinioException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.List;

@Service
public class MinioService {

    private final MinioClient minioClient;
    private final String bucket;

    @Autowired
    public MinioService(MinioClient minioClient,
                        @Value("${minio.bucket}") String bucket) {
        this.minioClient = minioClient;
        this.bucket = bucket;
    }

    public Mono<Void> uploadFile(FilePart file, String path) {

        return Mono.fromCallable(() -> {
                  //  getInputStreamFromFluxDataBuffer()
                    InputStream inputStream = file.content()
                            .map(DataBuffer::asInputStream).blockFirst();

                    minioClient.putObject(
                            PutObjectArgs.builder()
                                    .bucket(bucket)
                                    .object(path)
                                    .stream(inputStream, -1, 5 * 1024 * 1024) // 5MB chunks
                                    .contentType(file.headers().getContentType().toString())
                                    .build()
                    );

                    return (Void) null;
                })
                .subscribeOn(Schedulers.boundedElastic()); // IO goes off event loop
    }

    public Mono<Void> deleteFile(String path) {
        return Mono.fromRunnable(() -> {
            try {
                 minioClient.removeObject(
                        RemoveObjectArgs.builder()
                                .bucket(bucket)
                                .object(path)
                                .build()
                );
            } catch (Exception e) {
                // return Mono.error(e)
                throw new MinioException("MinIO upload failed", e);
            }
        });
    }

    public Flux<String> getAllFiles() {
        return Flux.defer(() -> {
            try {
                List<Bucket> buckets = minioClient.listBuckets(); // блокирующий вызов
                return Flux.fromIterable(
                        buckets.stream().map(Bucket::name).toList()
                );
            } catch (Exception e) {
                return Flux.error(new MinioException("oi getAllFiles", e));
            }
        }).subscribeOn(Schedulers.boundedElastic());
    }

    InputStream getInputStreamFromFluxDataBuffer(Flux<DataBuffer> data) throws IOException {
        PipedOutputStream osPipe = new PipedOutputStream();
        PipedInputStream isPipe = new PipedInputStream(osPipe);

        DataBufferUtils.write(data, osPipe)
                .subscribeOn(Schedulers.boundedElastic())
                .doOnComplete(() -> {
                    try {
                        osPipe.close();
                    } catch (IOException ignored) {
                    }
                })
                .subscribe(DataBufferUtils.releaseConsumer());
        return isPipe;
    }
}
