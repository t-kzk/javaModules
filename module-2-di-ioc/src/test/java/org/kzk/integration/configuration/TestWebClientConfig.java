package org.kzk.integration.configuration;


import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import reactor.core.publisher.Mono;

@TestConfiguration
@Slf4j
public class TestWebClientConfig {

/*
    @Bean
    @Primary
    public WebTestClient webTestClient(ApplicationContext context) {
        return WebTestClient.bindToApplicationContext(context)
                .configureClient()
                .filter(logRequest())
                .filter(logResponse())
                .build();
    }*/

    private ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(req -> {
            log.info("REQUEST {} {}", req.method(), req.url());
            return Mono.just(req);
        });
    }

    private ExchangeFilterFunction logResponse() {
        return ExchangeFilterFunction.ofResponseProcessor(res -> {
            log.info("RESPONSE {}", res.statusCode());
            return Mono.just(res);
        });
    }
}
