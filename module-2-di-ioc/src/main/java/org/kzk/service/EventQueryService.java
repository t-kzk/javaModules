package org.kzk.service;


import lombok.RequiredArgsConstructor;
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

@Service
@RequiredArgsConstructor
public class EventQueryService {

    private final EventService eventService;
    private final UserService userService;
    private final FileService fileService;
    private final EventMapper eventMapper;
    private final UserMapper userMapper;
    private final FileMapper fileMapper;

    Map<Integer, Mono<UserDto>> usersCache = new ConcurrentHashMap<>();
    Map<Integer, Mono<FileInfoDto>> filesCache = new ConcurrentHashMap<>();


    public Mono<EventDto> getEventById(Integer id) {
        return eventService.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Event not found!"
                )))
                .flatMap(event ->
                        Mono.zip(
                                userService.findById(event.getUserId()),
                                fileService.getFileById(event.getFileId())
                        ).map(tuple -> {
                                    EventDto eventDto = eventMapper.map(event);
                                    eventDto.setUser(userMapper.map(tuple.getT1()));
                                    eventDto.setFile(fileMapper.map(tuple.getT2()));

                                    return eventDto;
                                }
                        ));
    }

    public Flux<EventDto> getEvents() {
        return eventService.findAll().flatMap(event -> {
                    Mono<UserDto> userDtoMono = usersCache.computeIfAbsent(event.getUserId(),
                            key -> userService.findById(event.getUserId())
                                    .map(userMapper::map));

                    Mono<FileInfoDto> fileInfoDtoMono = filesCache.computeIfAbsent(event.getFileId(),
                            key -> fileService.getFileById(key)
                                    .map(fileMapper::map));

                    return Mono.zip(userDtoMono, fileInfoDtoMono)
                            .map(tuple -> {
                                EventDto eventDto = eventMapper.map(event);
                                eventDto.setUser(tuple.getT1());
                                eventDto.setFile(tuple.getT2());

                                return eventDto;
                            });
                }
        );
    }
}
