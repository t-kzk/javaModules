package org.kzk.service;


import lombok.extern.slf4j.Slf4j;
import org.kzk.data.EventRepository;
import org.kzk.data.entity.Event;
import org.kzk.data.entity.status.EventStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class EventService {

    private final EventRepository eventsRepository;

    @Autowired
    public EventService(EventRepository eventsRepository) {
        this.eventsRepository = eventsRepository;
    }

    @Transactional
    public Mono<Event> createEvent(Event event) {
        return eventsRepository.save(event);
    }

    public Mono<Integer> updateEventStatus(Integer fileId, EventStatus eventStatus) {
      return   eventsRepository.updateStatusById(fileId, eventStatus);
    }
}
