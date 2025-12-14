package org.kzk.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kzk.data.EventRepository;
import org.kzk.data.entity.EventEntity;
import org.kzk.data.entity.status.EventStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventsRepository;

    @Override
    public Mono<EventEntity> createEvent(
            Integer userId,
            Integer fileId,
            EventStatus status
    ) {
        return eventsRepository.save(
                        EventEntity.builder()
                                .userId(userId)
                                .fileId(fileId)
                                .status(status)
                                .build())
                .onErrorResume(ex -> Mono.error(new RuntimeException("sorry!")));
    }

}
