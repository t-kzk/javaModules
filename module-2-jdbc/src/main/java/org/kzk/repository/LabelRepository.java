package org.kzk.repository;

import org.kzk.model.Label;

import java.sql.Connection;
import java.util.List;

public interface LabelRepository extends ReadRepository<Label, Integer>{

    int addLabelsToPost(List<Integer> labelIds, Integer postId, Connection connection);
}
