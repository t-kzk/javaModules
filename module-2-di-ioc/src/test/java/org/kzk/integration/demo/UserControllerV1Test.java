package org.kzk.integration.demo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kzk.data.AuthRepository;
import org.kzk.data.UserRepository;
import org.kzk.dto.AuthRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

@ActiveProfiles("local")
@SpringBootTest(
        classes = TestConfig.class,

        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerV1Test {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthRepository authRepository;

    @Autowired
    private WebTestClient webTestClient;

    @BeforeEach
    public void setUp() {
        authRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void registrationPositive() {
        AuthRequestDto test1 = AuthRequestDto.builder()
                .username("test1")
                .password("1234")
                .build();

        WebTestClient.ResponseSpec result = webTestClient.post()
                .uri("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(test1).exchange();

        result.expectStatus().isOk();
    }
}
