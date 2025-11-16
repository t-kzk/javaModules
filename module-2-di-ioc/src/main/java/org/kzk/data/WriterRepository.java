package org.kzk.data;

import org.kzk.data.entity.Writer;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface WriterRepository extends R2dbcRepository<Writer, Integer> {
    Flux<Writer> findAll();

    Mono<Writer> findByUsername(String username);
}
