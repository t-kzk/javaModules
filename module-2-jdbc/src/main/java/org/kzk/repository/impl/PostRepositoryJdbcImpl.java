package org.kzk.repository.impl;

import org.kzk.env.db.DataSourceProviderMySql;
import org.kzk.model.Label;
import org.kzk.model.Post;
import org.kzk.model.PostStatus;
import org.kzk.repository.LabelRepository;
import org.kzk.repository.PostRepository;

import javax.sql.DataSource;
import java.sql.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;

public class PostRepositoryJdbcImpl implements PostRepository {

    private LabelRepository labelRepository = new LabelRepositoryJdbcImpl(); //todo

    private final DataSource ds = DataSourceProviderMySql.INSTANCE.getDataSource();

    private static final String SQL_FIND_ALL_BY_WRITER_ID = """
            SELECT * FROM posts WHERE writer_id = ? AND status != ?;
            """;

    private static final String SQL_SET_STATUS_BY_POST_ID = """
            UPDATE posts SET status = ? WHERE id = ?;
            """;

    private static final String SQL_UPDATE_CONTENT_BY_ID = """
            UPDATE posts SET content = ?, updated = ?, status = ? WHERE id = ?;
            """;

    private static final String SQL_INSERT_POST = """
            INSERT INTO posts (content, created, status, writer_id) 
            VALUES (?, ?, ?, ?)
            """;

    private static final String SQL_DELETE_BY_ID = """
            DELETE posts WHERE id = ?;
            """;

    private static final String SQL_FIND_BY_ID = """
            SELECT * FROM posts WHERE id = ?;
            """;
    private static final String SQL_FIND_ALL = """
            SELECT * FROM posts;
            """;

    @Override
    public List<Post> findAllByWriterId(Integer writerId) {
        List<Post> posts = new ArrayList<>();
        try (Connection connection = ds.getConnection();
             PreparedStatement ps = connection.prepareStatement(SQL_FIND_ALL_BY_WRITER_ID)
        ) {
            ps.setInt(1, writerId);
            ps.setString(2, PostStatus.DELETED.name());
            try (ResultSet resultSet = ps.executeQuery()) {
                while (resultSet.next()) {
                    posts.add(new Post(
                            resultSet.getInt("id"),
                            resultSet.getString("content"),
                            resultSet.getObject("created", LocalDateTime.class),
                            resultSet.getObject("updated", LocalDateTime.class),
                            new ArrayList<>(), // todo will add label
                            PostStatus.valueOf(resultSet.getString("status")),
                            writerId
                    ));
                }
            }
            return posts;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setStatusById(Integer postId, PostStatus postStatus) {
        try (Connection connection = ds.getConnection();
             PreparedStatement ps = connection.prepareStatement(SQL_SET_STATUS_BY_POST_ID)) {

            ps.setString(1, postStatus.name());
            ps.setInt(2, postId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int updatePostContentById(Integer id, String content, PostStatus postStatus) {
        Optional<Post> byId = findById(id);
        byId.ifPresent(e -> System.out.println("Найден! " + id));

        System.out.println("Все существуюшие");
        findAll().forEach(e -> System.out.println(e.id()));
        try (Connection connection = ds.getConnection();
             PreparedStatement ps = connection.prepareStatement(SQL_UPDATE_CONTENT_BY_ID)) {
            ps.setString(1, content);
            ps.setObject(2, Instant.now());
            ps.setString(3, postStatus.name());
            ps.setInt(4, id);
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
/*
    @Override
    public Integer save(Post entity) {

        try (Connection connection = ds.getConnection();
             PreparedStatement ps = connection.prepareStatement(
                     SQL_INSERT_POST,
                     Statement.RETURN_GENERATED_KEYS
             )) {
            connection.setAutoCommit(false);
            Integer id = new Random().nextInt();
            ps.setInt(1, id);
            ps.setString(2, entity.content());
            ps.setObject(3, entity.created());
            ps.setString(4, entity.status().name());
            ps.setString(5, entity.writerId().toString());
            ps.execute();

            List<Integer> labelIds = entity.labels().stream().map(Label::id).toList();
            labelRepository.addLabelsToPost(labelIds, id, connection);

            connection.commit();
            connection.setAutoCommit(true);
            return id;

        } catch (SQLException e) {
            connection.rollback();
            throw new RuntimeException(e);
        } finally {

        }
    }*/

    @Override
    public Integer save(Post entity) {
        Connection connection = null;
        try {
            connection = ds.getConnection();
            connection.setAutoCommit(false);


            Integer id = 0;
            // Вставка поста
            try (PreparedStatement ps = connection.prepareStatement(
                    SQL_INSERT_POST,
                    Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, entity.content());
                ps.setObject(2, entity.created());
                ps.setString(3, entity.status().name());
                ps.setString(4, entity.writerId().toString());
                ps.execute();


                try (ResultSet resultSet = ps.getGeneratedKeys()){
                    if (resultSet.next()) {
                        id= resultSet.getInt(1);
                    }
                }
            }


            // Добавление лейблов
            List<Integer> labelIds = entity.labels().stream()
                    .map(Label::id)
                    .toList();
            labelRepository.addLabelsToPost(labelIds, id, connection);

            connection.commit();
            return id;

        } catch (SQLException e) {
            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException ex) {
                throw new RuntimeException("Ошибка отката транзакции", ex);
            }
            throw new RuntimeException("Ошибка транзакции", e);
        } finally {
            try {
                if (connection != null) {
                    connection.setAutoCommit(true);
                    connection.close();
                }
            } catch (SQLException e) {
                System.out.println("Неудачная попытка connection.close() " + e.getMessage());
            }
        }
    }

    @Override
    public void deleteById(Integer id) {
        try (Connection connection = ds.getConnection();
             PreparedStatement ps = connection.prepareStatement(SQL_DELETE_BY_ID)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Post> findById(Integer id) {
        try (Connection connection = ds.getConnection();
             PreparedStatement ps = connection.prepareStatement(SQL_FIND_BY_ID)) {
            ps.setInt(1, id);
            Post post = null;
            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    post = new Post(
                            resultSet.getInt("id"),
                            resultSet.getString("content"),
                            resultSet.getObject("created", LocalDateTime.class),
                            resultSet.getObject("updated", LocalDateTime.class),
                            null, // todo add labels
                            PostStatus.valueOf(resultSet.getString("status")),
                            resultSet.getInt("writer_id")
                    );
                }
            }
            return Optional.ofNullable(post);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Post> findAll() {
        try (Connection connection = ds.getConnection();
             PreparedStatement ps = connection.prepareStatement(SQL_FIND_ALL);
             ResultSet resultSet = ps.executeQuery()) {
            List<Post> posts = new ArrayList<>();
            while (resultSet.next()) {
                Post post = new Post(
                        resultSet.getInt("id"),
                        resultSet.getString("content"),
                        resultSet.getObject("created", LocalDateTime.class),
                        resultSet.getObject("updated", LocalDateTime.class),
                        null, // todo add labels
                        PostStatus.valueOf(resultSet.getString("status")),
                        resultSet.getInt("writer_id")
                );

                posts.add(post);
            }
            return posts;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
