package org.kzk.integration.test;

import org.junit.jupiter.api.Test;
import org.kzk.data.entity.EventEntity;
import org.kzk.data.entity.status.EventStatus;
import org.kzk.data.entity.status.FileStatus;
import org.kzk.dto.AuthResponseDto;
import org.kzk.dto.FileInfoDto;
import org.kzk.integration.configuration.LifecycleSpecification;
import org.kzk.integration.configuration.junitExtension.annotation.ApiLogin;
import org.kzk.integration.configuration.junitExtension.annotation.GenerateFiles;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.test.StepVerifier;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class FileControllerV1Test extends LifecycleSpecification {

    private static final String basePath = "/api/v1/file";

    @Test
    @ApiLogin
    void uploadTest(AuthResponseDto token) {
        byte[] content = "hello world".getBytes(StandardCharsets.UTF_8);

        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("file", new ByteArrayResource(content))
                .filename("test.txt")
                .contentType(MediaType.TEXT_PLAIN);

        WebTestClient.ResponseSpec result = webTestClient.post()
                .uri(basePath)
                .headers(headers -> headers.setBearerAuth(token.getToken()))
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(builder.build()))
                .exchange();
        FileInfoDto response = result
                .expectBody(FileInfoDto.class)
                .returnResult()
                .getResponseBody();

        result.expectStatus().isOk();

        StepVerifier.create(eventRepository.findAll().collectList())
                .assertNext(events -> {
                    assertEquals(1, events.size(),
                            "Ожидался 1 event, но получено: " + events.size());

                    EventEntity event = events.get(0);
                    assertEquals(EventStatus.CREATED, event.getStatus());
                })
                .verifyComplete();
    }

    @Test
    @ApiLogin(
            files = @GenerateFiles(count = 2)
    )
    void gatAllFilesTest(AuthResponseDto token, List<FileInfoDto> files) {

        WebTestClient.ResponseSpec result = webTestClient.get()
                .uri(basePath)
                .headers(headers -> headers.setBearerAuth(token.getToken()))
                .exchange();
        List<FileInfoDto> response = result
                .expectStatus().isOk()
                .expectBodyList(FileInfoDto.class)
                .returnResult()
                .getResponseBody();

        assertNotNull(response);
        assertEquals(files.size(), response.size());
    }

    @Test
    @ApiLogin(
            files = @GenerateFiles(count = 1)
    )
    void deleteFileTest(AuthResponseDto token, List<FileInfoDto> files) {
        FileInfoDto fileForDel = files.get(0);
        WebTestClient.ResponseSpec result = webTestClient.delete()
                .uri(basePath + "/" + fileForDel.getId())
                .headers(headers -> headers.setBearerAuth(token.getToken()))
                .exchange();
        result
                .expectStatus().isOk();

        StepVerifier.create(fileRepository.findById(fileForDel.getId()))
                .assertNext(f ->
                        assertEquals(FileStatus.ARCHIVED, f.getStatus()))
                .verifyComplete();
    }
}
