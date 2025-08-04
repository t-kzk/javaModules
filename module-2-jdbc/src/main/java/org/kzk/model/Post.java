package org.kzk.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public record Post(Integer id,
                   String content,
                   LocalDateTime created,
                   LocalDateTime updated,
                   List<Label> labels,
                   PostStatus status,
                   Integer writerId) {

    public static Post rsToPost(ResultSet resultSet, List<Label> labels) throws SQLException {
        return new Post(
                resultSet.getInt("id"),
                resultSet.getString("content"),
                resultSet.getObject("created", LocalDateTime.class),
                resultSet.getObject("updated", LocalDateTime.class),
                new ArrayList<>(), // todo will add label
                PostStatus.valueOf(resultSet.getString("status")),
                resultSet.getInt("writer_id")
        );
    }
}
