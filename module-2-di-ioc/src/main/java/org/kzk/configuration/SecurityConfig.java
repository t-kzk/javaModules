package org.kzk.configuration;

import lombok.extern.slf4j.Slf4j;
import org.kzk.security.AuthenticationManager;
import org.kzk.security.BearerTokenServerAuthenticationConverter;
import org.kzk.security.JwtHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import reactor.core.publisher.Mono;

@Configuration
@EnableReactiveMethodSecurity
@Slf4j
public class SecurityConfig {

    @Value("${jwt.secret}")
    private String secret;

    private final String[] publicRoutes = {"/api/v1/auth/register", "/api/v1/auth/login"};

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http,
                                                         AuthenticationManager authManager) {

        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
                .authorizeExchange(exchange -> exchange
                        .pathMatchers(HttpMethod.OPTIONS).permitAll()
                        .pathMatchers(publicRoutes).permitAll()
                        .anyExchange().authenticated()
                )
                .exceptionHandling(spec -> spec.authenticationEntryPoint(
                                (swe, e) -> {
                                    log.error("IN securityWebFilterChain - UNAUTHORIZED error: {}", e.getMessage());
                                    return Mono.fromRunnable(() ->
                                            swe.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED));
                                }
                        ).accessDeniedHandler((swe, e) -> {
                                    log.error("IN securityWebFilterChain - accessDenied error: {}", e.getMessage());
                                    return Mono.fromRunnable(() ->
                                            swe.getResponse().setStatusCode(HttpStatus.FORBIDDEN));
                                }

                        )
                )
                .addFilterAt(bearerAuthFilter(authManager), SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }


    private AuthenticationWebFilter bearerAuthFilter(AuthenticationManager authenticationManager) {
        AuthenticationWebFilter bearerAuthFilter = new AuthenticationWebFilter(authenticationManager);

        bearerAuthFilter.setServerAuthenticationConverter(
                new BearerTokenServerAuthenticationConverter(new JwtHandler(secret))
        );

        bearerAuthFilter.setRequiresAuthenticationMatcher(
                ServerWebExchangeMatchers.pathMatchers(
                        "/api/v1/file/**",
                        "/api/v1/event/**",
                        "/api/v1/auth/info")
        );

        return bearerAuthFilter;
    }


}
