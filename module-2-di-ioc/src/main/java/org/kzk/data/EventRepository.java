package org.kzk.data;

import org.kzk.data.entity.Event;
import org.kzk.data.entity.status.EventStatus;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface EventRepository extends R2dbcRepository<Event, Integer> {

    @Query("UPDATE events SET status = :status WHERE file_id = :fileId")
    Mono<Integer> updateStatusById(Integer fileId, EventStatus status);
}
