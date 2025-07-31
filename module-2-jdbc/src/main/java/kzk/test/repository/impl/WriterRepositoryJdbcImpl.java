package kzk.test.repository.impl;

import kzk.test.env.db.DataSourceProviderMySql;
import kzk.test.model.Post;
import kzk.test.model.Writer;
import kzk.test.repository.WriterRepository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

public class WriterRepositoryJdbcImpl implements WriterRepository {

    private final DataSource ds = DataSourceProviderMySql.INSTANCE.getDataSource();
    private final PostRepositoryJdbcImpl postRepository = new PostRepositoryJdbcImpl();

    private final static String SQL_INSERT_WRITER = """
        INSERT INTO writers (id, first_name, last_name) 
        VALUES (?, ?, ?);
        """;
    private final static String SQL_DELETE_WRITER = """
            DELETE FROM writers WHERE id = ?;
            """;
    private final static String SQL_FIND_BY_ID_WRITER = """
            SELECT FROM writers WHERE id = ?;
            """;
    private final static String SQL_FIND_BY_NAME_WRITER = """
            SELECT FROM writers WHERE first_name = ?;
            """;
    private final static String SQL_FIND_ALL = """
            SELECT * FROM writers;
            """;

    @Override
    public Integer save(Writer entity) {

        try (Connection connection = ds.getConnection();
             PreparedStatement ps = connection.prepareStatement(
                     SQL_INSERT_WRITER,
                     Statement.RETURN_GENERATED_KEYS
             )) {
            Integer id = new Random().nextInt();
            ps.setInt(1, id);
            ps.setString(2, entity.firstName());
            ps.setString(3, entity.lastName());
            ps.executeUpdate();

           return id;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int deleteById(Integer id) {
        try (Connection connection = ds.getConnection();
             PreparedStatement ps = connection.prepareStatement(SQL_DELETE_WRITER)) {
            ps.setInt(1, id);
           return ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Writer> findById(Integer id) {
        Writer writer = null;
        try (Connection connection = ds.getConnection();
             PreparedStatement psWriter = connection.prepareStatement(SQL_FIND_BY_ID_WRITER);
        ) {
            psWriter.setInt(1, id);
            try (ResultSet resultSetWriter = psWriter.executeQuery()) {
                if (resultSetWriter.next()) {
                    writer = new Writer(
                            id,
                            resultSetWriter.getString(2),
                            resultSetWriter.getString(3),
                            postRepository.findAllByWriterId(id));
                }
            }
            return Optional.ofNullable(writer);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public Optional<Integer> findIdByFirstname(String firstName) {
        try (Connection connection = ds.getConnection();
             PreparedStatement psWriter = connection.prepareStatement(SQL_FIND_BY_NAME_WRITER);
        ) {
            Integer userid = null;
            psWriter.setString(1, firstName);
            try (ResultSet resultSetWriter = psWriter.executeQuery()) {
                if (resultSetWriter.next()) {
                    userid = resultSetWriter.getInt(1);
                }
            }

            return Optional.ofNullable(userid);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Writer> findAll() {
        try (Connection connection = ds.getConnection();
             PreparedStatement ps = connection.prepareStatement(SQL_FIND_ALL);
             ResultSet resultSet = ps.executeQuery()) {
            List<Writer> writers = new ArrayList<>();
            while (resultSet.next()) {
                Integer writerId = resultSet.getInt("id");
                List<Post> allByWriterId = postRepository.findAllByWriterId(writerId);
                writers.add(new Writer(
                        writerId,
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        allByWriterId

                ));
            }

            return writers;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
