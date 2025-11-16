package org.kzk.storage;

import io.minio.*;
import jakarta.annotation.PostConstruct;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.InputStream;
import java.util.Objects;

@Service
@Slf4j
public class MinioFileStorageImpl implements FileStorage {
    private MinioClient minioClient;

    @Value("${minio.bucket}")
    private String bucket;

    @Value("${minio.url}")
    private String url;

    @Value("${minio.access-key}")
    private String accessKey;

    @Value("${minio.secret-key}")
    private String secretKey;

    @PostConstruct
    @SneakyThrows
    public void init() {
        this.minioClient = MinioClient.builder()
                .endpoint(url)
                .credentials(accessKey, secretKey)
                .build();

        boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
        if (!found) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
            log.info("Created bucket: {}", bucket);
        }
    }

    @Override
    public Mono<Void> uploadFile(FilePart file, String path) {
        return file.content()
                .flatMap(buffer -> Mono.fromCallable(() -> {
                                    try (InputStream is = buffer.asInputStream()) {
                                        minioClient.putObject(
                                                PutObjectArgs.builder()
                                                        .bucket(bucket)
                                                        .object(path)
                                                        .stream(is, buffer.readableByteCount(), -1)
                                                        .contentType(file.headers().getContentType().toString())
                                                        .build()
                                        );
                                        return true;
                                    }
                                })
                                .subscribeOn(Schedulers.boundedElastic())
                                .doFinally(s -> DataBufferUtils.release(buffer))
                )
                .then();
    }

    @Override
    public Mono<Void> deleteFile(String path) {
        return Mono.fromCallable(() -> {
                    minioClient.removeObject(
                            RemoveObjectArgs.builder()
                                    .bucket(bucket)
                                    .object(path)
                                    .build()
                    );
                    return null;
                })
                .onErrorMap(e -> new RuntimeException("MinIO delete failed: " + e.getMessage(), e))
                .subscribeOn(Schedulers.boundedElastic())
                .then();
    }

    @Override
    public Flux<String> getFiles(String folderPath) {
        return Flux.fromIterable(minioClient.listObjects(
                        ListObjectsArgs.builder()
                                .bucket(bucket)
                                .prefix(folderPath.endsWith("/") ? folderPath : folderPath + "/")
                                .recursive(false) // показать только файлы внутри, без вложенных
                                .build()
                ))
                .publishOn(Schedulers.boundedElastic()) //
                .flatMap(result -> Mono.fromCallable(() -> {
                    // объект может быть директорией
                    if (result.get().isDir()) {
                        return null;
                    }
                    return result.get().objectName();
                }))
                .filter(Objects::nonNull);
    }

    @Override
    public Mono<Resource> getFile(String path) {
        return Mono.fromCallable(() ->
                        minioClient.getObject(
                                GetObjectArgs.builder()
                                        .bucket(bucket)
                                        .object(path)
                                        .build()
                        )
                )
                .subscribeOn(Schedulers.boundedElastic())
                .map(inputStream -> new InputStreamResource(inputStream));
    }
}
