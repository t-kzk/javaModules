package org.kzk.service;

import org.kzk.data.entity.UserEntity;
import reactor.core.publisher.Mono;

public interface UserService {

    Mono<UserEntity> createUser(
            String username,
            String passwordRaw);

    Mono<UserEntity> findByName(String name);

    Mono<UserEntity> findById(Integer id);

    Mono<Void> deleteUser(Integer userId);
}
