package org.kzk.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kzk.model.Label;
import org.kzk.repository.LabelRepository;
import org.kzk.service.LabelService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LabelServiceTest {
    @Mock
    private LabelRepository labelRepository;

    @InjectMocks
    private LabelService labelService;


    @Test
    void findAllLabelsTest() {
        List<Label> labels = List.of(
                new Label(1, "LabelName1", new ArrayList<>()),
                new Label(2, "LabelName2", new ArrayList<>())
        );
        when(labelRepository.findAll()).thenReturn(labels);

        List<Label> actualLabels = labelService.findAllLabels();

        assertEquals(labels, actualLabels);
    }

    @Test
    void findByIdPositiveTest() {
        Label label = new Label(1, "LabelName1", new ArrayList<>());
        when(labelRepository.findById(any())).thenReturn(Optional.ofNullable(label));

        Label actualLabel = labelService.findLabelById(1);

        assertEquals(label, actualLabel);
    }

    @Test
    void findByIdNegativeTest() {

        when(labelRepository.findById(any())).thenReturn(Optional.ofNullable(null));

        assertThrowsExactly(RuntimeException.class, () -> labelService.findLabelById(1));


    }


}
