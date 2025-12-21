package org.kzk.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kzk.data.EventRepository;
import org.kzk.data.entity.EventEntity;
import org.kzk.data.entity.UserEntity;
import org.kzk.data.entity.status.EventStatus;
import org.kzk.dto.EventDto;
import org.kzk.dto.FileInfoDto;
import org.kzk.dto.UserDto;
import org.kzk.mapper.EventMapper;
import org.kzk.mapper.FileMapper;
import org.kzk.mapper.UserMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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

    @Override
    public Mono<EventEntity> findById(Integer id) {
        return eventsRepository.findById(id);
    }

    @Override
    public Flux<EventEntity> findAll() {
        return eventsRepository.findAll();
    }


}
