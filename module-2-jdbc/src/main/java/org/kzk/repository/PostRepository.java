package org.kzk.repository;

import org.kzk.model.Post;
import org.kzk.model.PostStatus;

import java.util.List;

public interface PostRepository extends CrudRepository<Post, Integer> {

    List<Post> findAllByWriterId(Integer writerId);

    int updatePostContentById(Integer id, String content, PostStatus postStatus);

    void setStatusById(Integer postId, PostStatus postStatus);

    void deleteById(Integer uuid);
}

