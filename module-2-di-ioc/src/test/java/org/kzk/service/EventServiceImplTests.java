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

import java.io.IOException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
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
                    assertThat(ev.getStatus()).isEqualTo(EventStatus.CREATED);
                    assertThat(ev.getUserId()).isEqualTo(1L);
                    assertThat(ev.getFileId()).isEqualTo(2L);
                })
                .verifyComplete();
    }

    @Test
    void givenEventToSave_whenSaveEvent_thenRepositoryIsNotCalled() {
        //given
        Mockito.when(eventsRepository.save(any(EventEntity.class)))
                .thenReturn(Mono.error(new IOException("oii")));
        //when
        Mono<EventEntity> event = eventService.createEvent(1, 2, EventStatus.CREATED);
        //then
        StepVerifier.create(event)
                .expectErrorMatches(throwable ->
                        throwable instanceof RuntimeException &&
                                throwable.getMessage().equals("sorry!"))
                .verify();
    }

}
