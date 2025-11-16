package org.kzk.service;


import lombok.extern.slf4j.Slf4j;
import org.kzk.data.FileRepository;
import org.kzk.data.entity.Event;
import org.kzk.data.entity.FileE;
import org.kzk.data.entity.status.EventStatus;
import org.kzk.data.entity.status.FileStatus;
import org.kzk.minio.MinioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class FileService {

    private final FileRepository filesRepository;
    private final EventService eventService;
    private final MinioService minioService;

    @Autowired
    public FileService(
            FileRepository filesRepository,
            MinioService minioService,
            EventService eventService) {
        this.filesRepository = filesRepository;
        this.minioService = minioService;
        this.eventService = eventService;
    }

    @Transactional
    public Mono<FileE> createFile(
            Integer userId,
            FilePart file) {

        String filename = file.filename();
        String path = String.join("/", userId.toString(), filename);

        return minioService.uploadFile(file, path)
                .then(
                        Mono.defer(() -> {
                            FileE fileEntity = FileE.builder()
                                    .name(filename)
                                    .location(path)
                                    .status(FileStatus.ACTIVE)
                                    .build();

                            return filesRepository.save(fileEntity)
                                    .flatMap(savedFile -> {
                                        Event event = Event.builder()
                                                .writerId(userId)
                                                .fileId(savedFile.getId())
                                                .status(EventStatus.CREATED)
                                                .time(OffsetDateTime.now())
                                                .build();

                                        return eventService.createEvent(event)
                                                .thenReturn(savedFile);
                                    });
                        })
                        // todo в случае проблемы, поддержим откат транзакции удалением файла
                ).onErrorResume(e -> minioService.deleteFile(path).then(Mono.error(e)));
    }

    @Transactional
    public Mono<Void> deleteFile(int fileId) {
        return filesRepository.findById(fileId)
                .flatMap(file -> {
                    String location = file.getLocation();

                    // Обновляем статус файла
                    Mono<Integer> updateFileStatus = filesRepository.updateStatusById(fileId, FileStatus.ARCHIVED);

                    // Обновляем событие
                    Mono<Integer> updateEventStatus = eventService.updateEventStatus(fileId, EventStatus.DELETED);

                    // Удаляем файл из MinIO
                    Mono<Void> deleteFromMinio = minioService.deleteFile(location);

                    // Объединяем все операции последовательно, будут исполняться здесь
                    return updateFileStatus
                            .then(updateEventStatus)
                            .then(deleteFromMinio);
                });
    }
/* todo
    public Mono<Event> updateFile(FilePart file, Integer userId){

        String filename = file.filename();
        String path = String.join("/", userId.toString(), filename);

        return minioService.uploadFile(file, path)
                .then(
                        Mono.defer(() -> {
                            FileE fileEntity = filesRepository.findBy()

                            return  eventService.createEvent(event)
                                    .thenReturn(savedFile);
                        })
                        // todo в случае проблемы, поддержим откат транзакции удалением файла
                ).onErrorResume(e -> minioService.deleteFile(path).then(Mono.error(e)));
    }*/

    public Flux<FileE> findAll() {
        return filesRepository.findAll();
    }

    public Mono<FileE> findById(int fileId) {
        return filesRepository.findById(fileId);
    }


}
