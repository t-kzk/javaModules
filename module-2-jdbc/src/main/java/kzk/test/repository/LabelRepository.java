package kzk.test.repository;

import kzk.test.model.Label;

import java.sql.Connection;
import java.util.List;

public interface LabelRepository extends ReadRepository<Label, Integer>{

    int addLabelsToPost(List<Integer> labelIds, Integer postId, Connection connection);
}
