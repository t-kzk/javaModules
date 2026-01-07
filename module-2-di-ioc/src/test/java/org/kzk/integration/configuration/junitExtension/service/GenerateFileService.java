package org.kzk.integration.configuration.junitExtension.service;

import net.datafaker.Faker;
import org.jetbrains.annotations.NotNull;
import org.kzk.dto.FileInfoDto;
import org.kzk.integration.configuration.junitExtension.annotation.ApiRegistration;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.nio.charset.StandardCharsets;

public class GenerateFileService {
    private static Faker faker = new Faker();

    public FileInfoDto generateFile(@NotNull String token, WebTestClient webTestClient) {

        String fileTestInfo = faker.file().fileName();
        byte[] content = fileTestInfo.getBytes(StandardCharsets.UTF_8);

        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("file", new ByteArrayResource(content))
                .filename(fileTestInfo+".txt")
                .contentType(MediaType.TEXT_PLAIN);

        WebTestClient.ResponseSpec result = webTestClient.post()
                .uri("/api/v1/file")
                .headers(headers -> headers.setBearerAuth(token))
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(builder.build()))
                .exchange();
        FileInfoDto response = result
                .expectBody(FileInfoDto.class)
                .returnResult()
                .getResponseBody();

        return response;
    }
}
