package org.kzk.integration.configuration.junitExtension.service;

import net.datafaker.Faker;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.kzk.dto.AuthRequestDto;
import org.kzk.dto.UserDto;
import org.kzk.integration.configuration.junitExtension.annotation.ApiRegistration;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Objects;


public class GenerateUserService {

    private static Faker faker = new Faker();

    public Pair<AuthRequestDto, UserDto> generateUser(@NotNull ApiRegistration annotation, WebTestClient webTestClient) {

        String username;
        String password;

        if(Objects.equals(annotation.login(), "")) {
            username = faker.gameOfThrones().dragon();
        } else {
            username = annotation.login();
        }


        if(Objects.equals(annotation.login(), "")) {
            password = faker.number().digits(5);
        } else {
            password = annotation.password();
        }

        AuthRequestDto requestDto = AuthRequestDto.builder()
                .username(username)
                .password(password)
                .build();

        UserDto userInfo = doRegistration(webTestClient, requestDto);
        return Pair.of(requestDto, userInfo);
    }


    private UserDto doRegistration(WebTestClient webClient, AuthRequestDto requestDto) {
        return webClient.post()
                .uri("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestDto)
                .exchange().expectBody(UserDto.class)
                .returnResult()
                .getResponseBody();
    }
}
