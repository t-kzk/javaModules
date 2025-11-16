package org.kzk.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kzk.data.AuthRepository;
import org.kzk.data.UserRepository;
import org.kzk.data.entity.AuthEntity;
import org.kzk.data.entity.UserEntity;
import org.kzk.data.entity.UserRole;
import org.kzk.data.entity.status.UserStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl {
    private final UserRepository writersRepository;
    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TransactionalOperator reactiveTx;


    public Mono<UserEntity> createUser(
            String username,
            String passwordRaw) {
        return reactiveTx.transactional(writersRepository.save(UserEntity.builder()
                                .status(UserStatus.ACTIVE)
                                .username(username)
                                .build())
                        .flatMap(user ->
                                authRepository.save(AuthEntity.builder()
                                        .userId(user.getId())
                                        .passwordHash(passwordEncoder.encode(passwordRaw))
                                        .role(UserRole.USER_ROLE)
                                        .build()
                                ).thenReturn(user)
                        ))
                .doOnSuccess(u -> log.info("User created: {}", u));
    }

    public Mono<UserEntity> findByName(String name) {
        return writersRepository.findByUsername(name);
    }

    public Mono<UserEntity> findById(Integer id) {
        return writersRepository.findById(id);
    }

    public Mono<Void> deleteUser(Integer userId) {
        return writersRepository.findById(userId).flatMap(user -> {
                    user.setStatus(UserStatus.BLOCKED);
                    return reactiveTx.transactional(userRepository.save(user));
                }
        ).then();
    }
}
