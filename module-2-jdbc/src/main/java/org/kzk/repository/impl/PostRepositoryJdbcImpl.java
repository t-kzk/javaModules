package org.kzk.repository.impl;

import org.kzk.helper.JdbcHelper;
import org.kzk.model.Label;
import org.kzk.model.Post;
import org.kzk.model.PostStatus;
import org.kzk.repository.PostRepository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.kzk.helper.JdbcHelper.getPreparedStatement;
import static org.kzk.helper.JdbcHelper.getPreparedStatementWithKeys;

public class PostRepositoryJdbcImpl implements PostRepository {

    private static final String SQL_FIND_ALL_BY_WRITER_ID = """
            SELECT * FROM posts WHERE writer_id = ? AND status != ?;
            """;

    private static final String SQL_UPDATE_BY_POST_ID = """
            UPDATE posts SET content =?, status = ? WHERE id = ?;
            """;

    private static final String SQL_INSERT_POST_LABEL = """
            INSERT INTO post_labels (post_id, label_id)
            VALUES(?, ?)
            """;

    private static final String SQL_INSERT_POST = """
            INSERT INTO posts (content, status, writer_id) 
            VALUES (?, ?, ?)
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
        try (PreparedStatement ps = getPreparedStatement(SQL_FIND_ALL_BY_WRITER_ID)) {
            ps.setInt(1, writerId);
            ps.setString(2, PostStatus.DELETED.name());
            try (ResultSet resultSet = ps.executeQuery()) {
                while (resultSet.next()) {
                    int postId = resultSet.getInt("id");
                    // Optional<Label> byId = labelRepository.findById(postId);
                    // todo will add label
                    posts.add(Post.rsToPost(resultSet, new ArrayList<>()));
                }
            }
            return posts;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Post update(Post post) {
        try (PreparedStatement ps = getPreparedStatementWithKeys(SQL_UPDATE_BY_POST_ID)) {
            ps.setString(1, post.content());
            ps.setString(2, post.status().name());
            ps.setInt(3, post.id());
            int updatedRow = ps.executeUpdate();
            if (updatedRow > 0) {
                Optional<Post> postOpt = findById(post.id());
                return postOpt.get();
            } else {
                throw new RuntimeException("Post not updated");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Post save(Post entity) {
        try (PreparedStatement psPost = getPreparedStatementWithKeys(SQL_INSERT_POST);
             PreparedStatement psLabels = getPreparedStatement(SQL_INSERT_POST_LABEL)) {

            JdbcHelper.beginTransaction();

            psPost.setString(1, entity.content());
            psPost.setString(2, entity.status().name());
            psPost.setString(3, entity.writerId().toString());
            psPost.execute();
            Integer postId;
            try (ResultSet resultSet = psPost.getGeneratedKeys()) {
                if (resultSet.next()) {
                    postId = resultSet.getInt(1);
                } else {
                    throw new SQLException("Creating post failed, no ID present");
                }
            }

            List<Label> labels = entity.labels();
            if (!labels.isEmpty()) {
                for (Label label : labels) {
                    psLabels.setInt(1, postId);
                    psLabels.setInt(2, label.id());
                    psLabels.addBatch();
                }

                int[] results = psLabels.executeBatch();
                int countAddedLabels = Arrays.stream(results).sum();
                if (countAddedLabels != labels.size()) {
                    throw new SQLException("Count labels from entity = %d; Count added labels = %d"
                            .formatted(labels.size(), countAddedLabels));
                }
            }
            JdbcHelper.commitTransaction();

            return findById(postId).get();
        } catch (SQLException e) {
            JdbcHelper.rollbackTransaction();
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean delete(Post post) {
        try (PreparedStatement ps = getPreparedStatement(SQL_DELETE_BY_ID)) {
            ps.setInt(1, post.id());
            return ps.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Post> findById(Integer id) {
        try (PreparedStatement ps = getPreparedStatement(SQL_FIND_BY_ID)) {
            ps.setInt(1, id);
            Post post = null;
            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    // todo add labels
                    post = Post.rsToPost(resultSet, new ArrayList<>());
                }
            }
            return Optional.ofNullable(post);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Post> findAll() {
        try (PreparedStatement ps = getPreparedStatement(SQL_FIND_ALL);
             ResultSet resultSet = ps.executeQuery()) {
            List<Post> posts = new ArrayList<>();
            while (resultSet.next()) {
                // todo add labels
                Post post = Post.rsToPost(resultSet, new ArrayList<>());
                posts.add(post);
            }
            return posts;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
