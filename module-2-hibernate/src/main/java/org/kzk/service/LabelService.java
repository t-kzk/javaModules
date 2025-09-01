package org.kzk.service;

import org.kzk.model.Label;
import org.kzk.repository.LabelRepository;
import org.kzk.repository.imp.HibernateLabelRepositoryImpl;

import java.util.List;
import java.util.Set;

public class LabelService {

    private final LabelRepository labelRepository;

    public LabelService() {
        this.labelRepository = new HibernateLabelRepositoryImpl();
    }

    public LabelService(LabelRepository labelRepository) {
        this.labelRepository = labelRepository;
    }

    public List<Label> findAllLabels() {
        return labelRepository.findAll();
    }

    public Label findLabelById(Integer labelId) {
        return labelRepository.findById(labelId)
                .orElseThrow(() -> new RuntimeException("Not exist this label %d".formatted(labelId)));
    }

    public Set<Label> findAllLabelsByIds(Set<Integer> labelIds) {

        return labelRepository.findLabelsByIds(labelIds);
    }
}
