package org.kzk.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kzk.TestData;
import org.kzk.data.FileRepository;
import org.kzk.data.entity.EventEntity;
import org.kzk.data.entity.FileEntity;
import org.kzk.data.entity.status.EventStatus;
import org.kzk.data.entity.status.FileStatus;
import org.kzk.storage.FileStorage;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.transaction.reactive.TransactionalOperator;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Objects;

import static org.kzk.TestData.FILE_PATH;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FileServiceImplTest {

    @Mock
    private FileRepository fileRepository;

    @Mock
    private EventService eventService;

    @Mock
    private FileStorage fileStorage;

    @Mock
    private TransactionalOperator reactiveTx;

    @InjectMocks
    FileServiceImpl fileService;


    @Test
    void givenFileForUpload_whenSaveFile_thenFileStorageIsCalled() {
        // given
        FilePart filePart = mock(FilePart.class);
        when(filePart.filename()).thenReturn(FILE_PATH);

        FileEntity savedFile = TestData.fileWithStatusActive();

        when(fileStorage.uploadFile(any(), any()))
                .thenReturn(Mono.empty());

        when(fileRepository.save(any()))
                .thenReturn(Mono.just(savedFile));

        when(eventService.createEvent(1, 10, EventStatus.CREATED))
                .thenReturn(Mono.empty());

        when(reactiveTx.transactional(any(Mono.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        // when
        Mono<FileEntity> result = fileService.uploadFile(1, filePart);

        // then
        StepVerifier.create(result)
                .expectNext(savedFile)
                .verifyComplete();
    }

    @Test
    void uploadFile_whenError_shouldDeleteFile() {
        FilePart filePart = mock(FilePart.class);
        when(filePart.filename()).thenReturn(FILE_PATH);
        when(fileRepository.save(any()))
                .thenReturn(Mono.error(new RuntimeException()));
        when(fileStorage.uploadFile(any(), any()))
                .thenReturn(Mono.empty());

        when(fileStorage.deleteFile(any()))
                .thenReturn(Mono.empty());

        when(reactiveTx.transactional(any(Mono.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        Mono<FileEntity> publisher = fileService.uploadFile(1, filePart);
        StepVerifier.create(publisher)
                .expectError(RuntimeException.class)
                .verify();

        verify(fileStorage).deleteFile("1/%s".formatted(FILE_PATH));
    }


    @Test
    void givenFileForUpdate_whenUpdateFile_thenRepositoryCalled() {
        // given
        FileEntity file = TestData.fileWithStatusActive();
        FilePart filePart = mock(FilePart.class);

        when(fileRepository.findById(file.getId()))
                .thenReturn(Mono.just(file));

        when(fileStorage.uploadFile(any(FilePart.class), eq(file.getLocation())))
                .thenReturn(Mono.empty());

        when(eventService.createEvent(1, file.getId(), EventStatus.UPDATED))
                .thenReturn(Mono.just(EventEntity.builder().build()));

        // when
        Mono<FileEntity> result = fileService.updateFile(1, file.getId(), filePart);

        // then
        StepVerifier.create(result)
                .expectNext(file)
                .verifyComplete();
    }

    @Test
    void givenFileForUpdate_whenError_then404() {
        // given
        FileEntity file = TestData.fileWithStatusActive();
        FilePart filePart = mock(FilePart.class);

        when(fileRepository.findById(file.getId()))
                .thenReturn(Mono.empty());

        // when
        Mono<FileEntity> result = fileService.updateFile(1, file.getId(), filePart);

        // then
        StepVerifier.create(result)
                .expectErrorMatches(ex ->
                        ex instanceof ResponseStatusException statusEx &&
                                Objects.equals(statusEx.getReason(), "File not found") &&
                                statusEx.getStatusCode().is4xxClientError())
                .verify();
    }


    @Test
    void givenFileForDelete_whenDeleteFile_thenFileStorageCalled() {
        // given
        FileEntity file = TestData.fileWithStatusActive();

        when(fileRepository.findById(file.getId()))
                .thenReturn(Mono.just(file));

        when(fileStorage.deleteFile(eq(file.getLocation())))
                .thenReturn(Mono.empty());

        when(reactiveTx.transactional(any(Mono.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        when(fileRepository.updateStatusById(file.getId(), FileStatus.ARCHIVED))
                .thenReturn(Mono.just(6666666));

        when(eventService.createEvent(1, file.getId(), EventStatus.DELETED))
                .thenReturn(Mono.just(EventEntity.builder().build()));

        // when
        Mono<Void> result = fileService.deleteFile(1, file.getId());

        // then
        StepVerifier.create(result)
                .verifyComplete();

        verify(fileStorage).deleteFile(file.getLocation());
        verify(fileRepository).updateStatusById(file.getId(), FileStatus.ARCHIVED);
        verify(eventService).createEvent(1, file.getId(), EventStatus.DELETED);
    }

    @Test
    void givenFileForDelete_whenFileIsNotExist_then404() {
        // given
        when(fileRepository.findById(666))
                .thenReturn(Mono.empty());


        // when
        Mono<Void> result = fileService.deleteFile(1, 666);

        // then
        StepVerifier.create(result)
                .expectErrorMatches(ex ->
                        ex instanceof ResponseStatusException statusEx &&
                                statusEx.getStatusCode().is4xxClientError())
                .verify();
    }
}
