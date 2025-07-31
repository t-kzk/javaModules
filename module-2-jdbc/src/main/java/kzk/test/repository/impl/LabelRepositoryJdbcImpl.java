package kzk.test.repository.impl;

import kzk.test.env.db.DataSourceProviderMySql;
import kzk.test.model.Label;
import kzk.test.repository.LabelRepository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class LabelRepositoryJdbcImpl implements LabelRepository {
    private final DataSource ds = DataSourceProviderMySql.INSTANCE.getDataSource();

    private static final String FIND_ALL = """
            SELECT * FROM labels;
            """;
    private static final String FIND_BY_ID = """
            SELECT * FROM labels where id = ?;
            """;
    private static final String ADD_LABEL_TO_POST = """
            INSERT INTO post_labels (post_id, label_id)
            VALUES (?, ?);
            """;

    @Override
    public int addLabelsToPost(List<Integer> labelIds, Integer postId, Connection connection) {
        try(PreparedStatement preparedStatement = connection.prepareStatement(ADD_LABEL_TO_POST)) {
            for (Integer lableId: labelIds) {
                preparedStatement.setInt(1, postId);
                preparedStatement.setInt(2, lableId);
                preparedStatement.addBatch();
            }
            int[] results = preparedStatement.executeBatch();
            System.out.println("Добавлено [%d] лейблов".formatted(Arrays.stream(results).sum()));
        } catch (SQLException e) {
          throw new RuntimeException(e);
      }
        return 0;
    }

    @Override
    public Optional<Label> findById(Integer integer) {
        try (Connection connection = ds.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID)) {
            preparedStatement.setInt(1, integer);
            Label label = null;
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    label = new Label(
                            resultSet.getInt("id"),
                            resultSet.getString("name")
                    );
                }
            }
            return Optional.ofNullable(label);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Label> findAll() {
        try (Connection connection = ds.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL);
             ResultSet resultSet = preparedStatement.executeQuery();) {
            List<Label> labels = new ArrayList<>();
            while (resultSet.next()) {
                labels.add(new Label(
                        resultSet.getInt("id"),
                        resultSet.getString("name")
                ));
            }
            return labels;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
