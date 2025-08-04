package org.kzk.repository.impl;

import org.kzk.model.Post;
import org.kzk.model.Writer;
import org.kzk.repository.WriterRepository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.kzk.helper.JdbcHelper.getPreparedStatement;
import static org.kzk.helper.JdbcHelper.getPreparedStatementWithKeys;

public class WriterRepositoryJdbcImpl implements WriterRepository {

    private final PostRepositoryJdbcImpl postRepository = new PostRepositoryJdbcImpl();

    private final static String SQL_INSERT_WRITER = """
            INSERT INTO writers (first_name, last_name) 
            VALUES (?, ?);
            """;
    private final static String SQL_DELETE_WRITER = """
            DELETE FROM writers WHERE id = ?;
            """;
    private final static String SQL_FIND_BY_ID_WRITER = """
            SELECT * FROM writers WHERE id = ?;
            """;
    private final static String SQL_FIND_ALL = """
            SELECT * FROM writers;
            """;
    private final static String SQL_UPDATE_WRITER = """
            UPDATE writers SET first_name = ?, last_name = ? 
            WHERE id = ?
            """;


    @Override
    public Writer save(Writer entity) {
        try (PreparedStatement ps = getPreparedStatementWithKeys(SQL_INSERT_WRITER)) {
            ps.setString(1, entity.firstName());
            ps.setString(2, entity.lastName());
            ps.executeUpdate();
            Integer id;
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    id = keys.getInt(1);
                } else {
                    throw new SQLException("Creating user failed, no ID present");
                }
            }
            Optional<Writer> writerOpt = findById(id);
            if (writerOpt.isPresent()) {
                return writerOpt.get();
            } else {
                throw new RuntimeException("User not found with id: " + id);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Writer update(Writer entity) {
        try (PreparedStatement ps = getPreparedStatement(SQL_UPDATE_WRITER)) {
            ps.setString(1, entity.firstName());
            ps.setString(2, entity.lastName());
            ps.setInt(3, entity.id());
            int updatedRow = ps.executeUpdate();

            if (updatedRow > 0) {
                Optional<Writer> writerOpt = findById(entity.id());
                return writerOpt.get();
            } else {
                throw new RuntimeException("Writer not updated");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean delete(Writer entity) {
        try (PreparedStatement ps = getPreparedStatement(SQL_DELETE_WRITER)) {
            ps.setInt(1, entity.id());
            return ps.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public Optional<Writer> findById(Integer id) {
        Writer writer = null;
        try (PreparedStatement psWriter = getPreparedStatement(SQL_FIND_BY_ID_WRITER)) {
            psWriter.setInt(1, id);
            try (ResultSet resultSetWriter = psWriter.executeQuery()) {
                if (resultSetWriter.next()) {
                    List<Post> allPostsByWriterId = postRepository.findAllByWriterId(id);
                    writer = Writer.rsToWriter(resultSetWriter, allPostsByWriterId);
                }
            }
            return Optional.ofNullable(writer);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Writer> findAll() {
        try (PreparedStatement ps = getPreparedStatement(SQL_FIND_ALL);
             ResultSet resultSet = ps.executeQuery()) {
            List<Writer> writers = new ArrayList<>();
            while (resultSet.next()) {
                Integer writerId = resultSet.getInt("id");
                List<Post> allByWriterId = postRepository.findAllByWriterId(writerId);
                writers.add(Writer.rsToWriter(resultSet, allByWriterId));
            }
            return writers;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
