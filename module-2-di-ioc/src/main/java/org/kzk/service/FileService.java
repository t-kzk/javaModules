package org.kzk.service;

import org.kzk.data.entity.FileEntity;
import org.springframework.core.io.Resource;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FileService {
    Mono<FileEntity> uploadFile(
            Integer userId,
            FilePart file
    );

    Mono<FileEntity> updateFile(
            Integer userId,
            Integer fileId,
            FilePart file
    );

    Mono<Void> deleteFile(int userId, int fileId);

    Flux<FileEntity> findAll();

    Flux<FileEntity> findAllByUserId(Integer userId);

    Mono<Resource> downloadFile(Integer fileId);

    Mono<FileEntity> getFileById(Integer fileId);
}
