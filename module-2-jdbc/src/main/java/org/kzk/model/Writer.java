package org.kzk.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public record Writer(Integer id,
                     String firstName,
                     String lastName,
                     List<Post> posts) {

    public static Writer rsToWriter(ResultSet resultSetWriter, List<Post> posts) throws SQLException {
        return new Writer(
                resultSetWriter.getInt(1),
                resultSetWriter.getString(2),
                resultSetWriter.getString(3),
                posts
        );
    }
}
