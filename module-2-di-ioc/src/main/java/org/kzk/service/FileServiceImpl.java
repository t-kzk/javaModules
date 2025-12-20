package org.kzk.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kzk.data.FileRepository;
import org.kzk.data.entity.FileEntity;
import org.kzk.data.entity.status.EventStatus;
import org.kzk.data.entity.status.FileStatus;
import org.kzk.storage.FileStorage;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final FileRepository filesRepository;
    private final EventService eventServiceImpl;
    private final FileStorage fileStorage;
    private final TransactionalOperator reactiveTx;

    @Override
    public Mono<FileEntity> uploadFile(
            Integer userId,
            FilePart file) {

        String filename = file.filename();
        String path = String.join("/", userId.toString(), filename);

        return fileStorage.uploadFile(file, path)
                .then(reactiveTx.transactional(filesRepository.save(FileEntity.builder()
                                        .name(filename)
                                        .location(path)
                                        .status(FileStatus.ACTIVE)
                                        .build())
                                .flatMap(savedFile ->
                                        eventServiceImpl.createEvent(
                                                        userId, savedFile.getId(),
                                                        EventStatus.CREATED)
                                                .thenReturn(savedFile))
                        ).onErrorResume(e -> fileStorage.deleteFile(path).then(Mono.error(e)))
                );

    }

    @Override
    public Mono<FileEntity> updateFile(
            Integer userId,
            Integer fileId,
            FilePart file
    ) {
        return filesRepository.findById(fileId)
                .switchIfEmpty(Mono.error(new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "File not found"
                )))
                .flatMap(fileEntity -> {
                    String location = fileEntity.getLocation();

                    return fileStorage.uploadFile(file, location)
                            .then(eventServiceImpl.createEvent(userId, fileId, EventStatus.UPDATED))
                            .thenReturn(fileEntity);
                });
    }

    @Override
    public Mono<Void> deleteFile(int userId, int fileId) {
        return filesRepository.findById(fileId)
                .switchIfEmpty(Mono.error(new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "File not found"
                )))
                .flatMap(file -> {
                    String location = file.getLocation();

                    return fileStorage.deleteFile(location) // минио
                            .then(reactiveTx.transactional( // бд в транзакции
                                    filesRepository.updateStatusById(fileId, FileStatus.ARCHIVED)
                                            .then(eventServiceImpl.createEvent(userId, fileId, EventStatus.DELETED))
                            ));
                }).then();
    }

    @Override
    public Flux<FileEntity> findAll() {
        return filesRepository.findAll();
    }

    @Override
    public Flux<FileEntity> findAllByUserId(Integer userId) {
        return filesRepository.findAllByUserId(userId);
    }

    @Override
    public Mono<Resource> downloadFile(Integer fileId) {
        return filesRepository.findById(fileId).flatMap(file -> {
            String location = file.getLocation();
            return fileStorage.getFile(location);
        }).switchIfEmpty(Mono.error(new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "File {%d} not found".formatted(fileId)
        )));
    }


}
