package org.kzk.service;

import org.kzk.model.Label;
import org.kzk.repository.LabelRepository;
import org.kzk.repository.impl.LabelRepositoryJdbcImpl;

import java.util.List;

public class LabelService {

    private final LabelRepository labelRepository = new LabelRepositoryJdbcImpl();

    public List<Label> findAllLabels() {
        return labelRepository.findAll();
    }

    public Label findLabelById(Integer labelId) {
        return labelRepository.findById(labelId)
                .orElseThrow(() -> new RuntimeException("Not exist this label %d".formatted(labelId)));
    }
}
