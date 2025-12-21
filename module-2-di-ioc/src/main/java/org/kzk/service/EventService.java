package org.kzk.service;

import org.kzk.data.entity.EventEntity;
import org.kzk.data.entity.status.EventStatus;
import org.kzk.dto.EventDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface EventService {
    Mono<EventEntity> createEvent(
            Integer userId,
            Integer fileId,
            EventStatus status
    );

    Mono<EventEntity> findById(Integer id);

    Flux<EventEntity> findAll();
}