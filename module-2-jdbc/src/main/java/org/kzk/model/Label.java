package org.kzk.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public record Label(
        Integer id,
        String name
) {

    public static Label rsToLabel(ResultSet resultSet) throws SQLException {
       return new Label(
                resultSet.getInt("id"),
                resultSet.getString("name")
        );
    }
}
