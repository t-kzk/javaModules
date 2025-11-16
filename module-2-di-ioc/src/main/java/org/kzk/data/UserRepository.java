package org.kzk.data;

import org.kzk.data.entity.UserAuthEntity;
import org.kzk.data.entity.UserEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserRepository extends R2dbcRepository<UserEntity, Integer> {
    Flux<UserEntity> findAll();

    Mono<UserEntity> findByUsername(String username);

    @Query("""
           SELECT u.id AS user_id,
                  u.username AS username,
                  u.status AS status,
                  a.password_hash AS password_hash,
                  a.role AS role
           FROM users u
           JOIN auth a ON u.id = a.user_id
           WHERE u.username = :username
           """)
    Mono<UserAuthEntity> findUserWithAuth(String username);
}
