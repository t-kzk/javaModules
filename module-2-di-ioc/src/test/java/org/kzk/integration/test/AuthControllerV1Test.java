package org.kzk.integration.test;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kzk.dto.AuthRequestDto;
import org.kzk.dto.AuthResponseDto;
import org.kzk.dto.UserDto;
import org.kzk.integration.configuration.LifecycleSpecification;
import org.kzk.integration.configuration.junitExtension.annotation.ApiLogin;
import org.kzk.integration.configuration.junitExtension.annotation.ApiRegistration;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

public class AuthControllerV1Test extends LifecycleSpecification {

    private static final String basePath = "/api/v1/auth";

    @Test
    @DisplayName("Registration new user")
    void registrationPositive() {
        AuthRequestDto test1 = AuthRequestDto.builder()
                .username("test1")
                .password("1234")
                .build();

        WebTestClient.ResponseSpec result = webTestClient.post()
                .uri(basePath + "/register")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(test1).exchange();

        UserDto response = result
                .expectBody(UserDto.class)
                .returnResult()
                .getResponseBody();

        result.expectStatus().isOk();

    }

    @Test
    @ApiRegistration()
    @DisplayName("LoginUser test")
    void loginPositive(UserDto userInfo, AuthRequestDto userPass) {
        AuthRequestDto test1 = AuthRequestDto.builder()
                .username(userPass.getUsername())
                .password(userPass.getPassword())
                .build();

        WebTestClient.ResponseSpec result = webTestClient.post()
                .uri(basePath + "/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(test1).exchange();

        AuthResponseDto response = result
                .expectBody(AuthResponseDto.class)
                .returnResult()
                .getResponseBody();

        result.expectStatus().isOk();

    }

    @Test
    @ApiLogin(
            registration = @ApiRegistration
    )
    @DisplayName("UserInfo test")
    void userInfo(UserDto userInfo, AuthRequestDto userPass, AuthResponseDto token) {
        WebTestClient.ResponseSpec result = webTestClient.get()
                .uri(basePath + "/info")
                .headers(headers -> headers.setBearerAuth(token.getToken()))
                .exchange();
        UserDto response = result
                .expectBody(UserDto.class)
                .returnResult()
                .getResponseBody();

        result.expectStatus().isOk();

    }
}
