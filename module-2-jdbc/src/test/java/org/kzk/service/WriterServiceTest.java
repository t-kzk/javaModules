package org.kzk.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kzk.model.Writer;
import org.kzk.repository.WriterRepository;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WriterServiceTest {

    @Mock
    WriterRepository writerRepository;

    @InjectMocks
    private WriterService writerService;

    @Test
    void createWriterTest() {
        Writer writer = new Writer(
                1,
                "firstname",
                "lastname",
                new ArrayList<>()
        );
        ArgumentCaptor<Writer> writerArgumentCaptor = ArgumentCaptor.forClass(Writer.class);
        when(writerRepository.save(writerArgumentCaptor.capture())).thenReturn(writer);

        Writer result = writerService.createWriter(writer.firstName(), writer.lastName());

        Writer value = writerArgumentCaptor.getValue();
        assertEquals(writer, result);
        assertEquals(writer.firstName(), value.firstName());
        assertEquals(writer.lastName(), value.lastName());
        assertEquals(null, value.id());
    }

    @Test
    void updateWriterTest() {
        Writer writer = new Writer(
                1,
                "firstname",
                "lastname",
                new ArrayList<>()
        );
        when(writerRepository.findById(1)).thenReturn(Optional.of(writer));
        ArgumentCaptor<Writer> argumentCaptor = ArgumentCaptor.forClass(Writer.class);
        when(writerRepository.update(argumentCaptor.capture())).thenReturn(writer);

        Writer result = writerService.updateWriter(1, "newFirstname", "newLastName");
        Writer value = argumentCaptor.getValue();

        assertEquals("newFirstname", value.firstName());
        assertEquals("newLastName", value.lastName());
    }

    @Test
    void updateWriterTestNegative() {

        when(writerRepository.findById(1)).thenReturn(Optional.ofNullable(null));

        assertThrowsExactly(RuntimeException.class,
                () -> writerService.updateWriter(1, "newFirstname", "newLastName"));

    }

    @Test
    void showAllWriters() {
        Writer writer = new Writer(
                1,
                "firstname",
                "lastname",
                new ArrayList<>()
        );
        List<Writer> writers = Collections.singletonList(writer);
        when(writerRepository.findAll()).thenReturn(writers);

        List<Writer> result = writerService.showAllWriters();

        assertEquals(writers, result);
    }

    @Test
    void deleteWriterTest() {
        Writer writer = new Writer(
                1,
                "firstname",
                "lastname",
                new ArrayList<>()
        );
        when(writerRepository.findById(1)).thenReturn(Optional.of(writer));
        when(writerRepository.delete(writer)).thenReturn(true);

        boolean result = writerService.deleteWriter(1);

        assertTrue(result);
    }
}
