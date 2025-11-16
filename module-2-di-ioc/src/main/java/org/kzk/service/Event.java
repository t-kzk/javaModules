package org.kzk.service;

import org.kzk.data.entity.EventEntity;
import org.kzk.data.entity.status.EventStatus;
import reactor.core.publisher.Mono;

public interface Event {
    Mono<EventEntity> createEvent(
            Integer userId,
            Integer fileId,
            EventStatus status
    );
}