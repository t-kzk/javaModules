package org.kzk.security;

import org.kzk.data.UserRepository;
import org.kzk.data.entity.UserEntity;
import org.kzk.security.ex.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class AuthenticationManager implements ReactiveAuthenticationManager {

    private final UserRepository userRepository;

    @Autowired
    public AuthenticationManager(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {

        CustomPrincipal principal = (CustomPrincipal) authentication.getPrincipal();
        return userRepository.findById(principal.getUserId())
                .filter(UserEntity::isEnabled).switchIfEmpty(
                        Mono.error(new UnauthorizedException("User disabled"))
                ).map(user -> authentication);
    }
}
