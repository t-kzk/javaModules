package org.kzk.repository.impl;

import org.kzk.model.Label;
import org.kzk.repository.LabelRepository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.kzk.helper.JdbcHelper.getPreparedStatement;

public class LabelRepositoryJdbcImpl implements LabelRepository {
    private static final String FIND_ALL = """
            SELECT * FROM labels;
            """;
    private static final String FIND_BY_ID = """
            SELECT * FROM labels where id = ?;
            """;


    @Override
    public Label save(Label entity) {
        return null;
    }

    @Override
    public Label update(Label entity) {
        return null;
    }

    @Override
    public boolean delete(Label entity) {
        return false;
    }

    @Override
    public Optional<Label> findById(Integer integer) {
        try (PreparedStatement preparedStatement = getPreparedStatement(FIND_BY_ID)) {
            preparedStatement.setInt(1, integer);
            Label label = null;
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    label = Label.rsToLabel(resultSet);
                }
            }
            return Optional.ofNullable(label);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Label> findAll() {
        try (PreparedStatement preparedStatement = getPreparedStatement(FIND_ALL);
             ResultSet resultSet = preparedStatement.executeQuery();) {
            List<Label> labels = new ArrayList<>();
            while (resultSet.next()) {
                labels.add(Label.rsToLabel(resultSet));
            }
            return labels;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
