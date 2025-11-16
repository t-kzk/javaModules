package org.kzk.storage;

import org.springframework.core.io.Resource;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FileStorage {

    Mono<Void> uploadFile(FilePart file, String path);

    Mono<Void> deleteFile(String path);

    Flux<String> getFiles(String folderName);

    Mono<Resource> getFile(String path);
}
