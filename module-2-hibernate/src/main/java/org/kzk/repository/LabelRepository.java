package org.kzk.repository;

import org.kzk.model.Label;

import java.util.Set;

public interface LabelRepository extends GenericRepository<Label, Integer>{

    Set<Label> findLabelsByIds(Set<Integer> labelIds);

}
