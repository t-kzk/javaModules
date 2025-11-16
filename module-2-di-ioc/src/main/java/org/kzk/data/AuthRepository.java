package org.kzk.data;

import org.kzk.data.entity.AuthEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface AuthRepository extends R2dbcRepository<AuthEntity, Integer> {

    Mono<AuthEntity> findByUserId(Integer userId);
}
