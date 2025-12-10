package org.kzk.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kzk.TestData;
import org.kzk.data.EventRepository;
import org.kzk.data.entity.EventEntity;
import org.kzk.data.entity.status.EventStatus;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class EventServiceImplTests {

    @Mock
    private EventRepository eventsRepository;

    @InjectMocks
    private EventServiceImpl eventService;

    @Test
    void givenEventToSave_whenSaveEvent_thenRepositoryIsCalled() {
        //given
        EventEntity eventEntity = TestData.eventWithStatusCreated();
        Mockito.when(eventsRepository.save(any(EventEntity.class)))
                .thenReturn(Mono.just(eventEntity));
        //when
        Mono<EventEntity> event = eventService.createEvent(1, 2, EventStatus.CREATED);
        //then
        StepVerifier.create(event)
                .expectNext(eventEntity)
                .verifyComplete();

        StepVerifier.create(event)
                .assertNext(ev -> {
                    assertEquals(EventStatus.CREATED, ev.getStatus());
                    /*assertEquals(1, ev.getUserId());
                    assertEquals(2, ev.getFileId());*/
                })
                .verifyComplete();
    }

}
