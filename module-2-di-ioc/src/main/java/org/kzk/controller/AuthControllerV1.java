package org.kzk.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kzk.dto.AuthRequestDto;
import org.kzk.dto.AuthResponseDto;
import org.kzk.dto.UserDto;
import org.kzk.dto.mapper.UserMapper;
import org.kzk.security.CustomPrincipal;
import org.kzk.security.SecurityService;
import org.kzk.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthControllerV1 {

    private final UserService userService;
    private final SecurityService securityService;
    private final UserMapper userMapper;

    @PostMapping("/register")
    public Mono<UserDto> register(@RequestBody AuthRequestDto user) {
        return userService.createUser(user.getUsername(), user.getPassword()).map(userMapper::map);
    }

    @PostMapping("/login")
    public Mono<AuthResponseDto> login(@RequestBody AuthRequestDto user) {
        return securityService.authenticate(user.getUsername(), user.getPassword())
                .flatMap(tokenDetails -> Mono.just(
                        AuthResponseDto.builder()
                                .userId(tokenDetails.getUserId())
                                .token(tokenDetails.getToken())
                                .issuedAt(tokenDetails.getIssuedAt())
                                .expiresAt(tokenDetails.getExpiresAt())
                                .build()
                ));
    }


    @GetMapping("/info")
    public Mono<UserDto> getUserInfo(Authentication authentication) {
        CustomPrincipal principal = (CustomPrincipal) authentication.getPrincipal();

        return userService.findByName(principal.getName()).map(userMapper::map);
    }
}
