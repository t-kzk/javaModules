package org.kzk.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kzk.dto.EventDto;
import org.kzk.service.EventQueryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/event")
public class EventControllerV1 {

    final EventQueryService eventService;

    @GetMapping("/{eventId}")
    public Mono<EventDto> getEvent(@PathVariable Integer eventId) {
        return eventService.getEventById(eventId);
    }

    @GetMapping()
    public Flux<EventDto> getEvents() {
        return eventService.getEvents();
    }
}
