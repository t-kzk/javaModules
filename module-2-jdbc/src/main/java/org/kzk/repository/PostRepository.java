package org.kzk.repository;

import org.kzk.model.Post;
import org.kzk.model.PostStatus;

import java.util.List;

public interface PostRepository extends CrudRepository<Post, Integer> {

    List<Post> findAllByWriterId(Integer writerId);

    // no
    int updatePostContentById(Integer id, String content, PostStatus postStatus);
//no
    void setStatusById(Integer postId, PostStatus postStatus);
//no
    void deleteById(Integer uuid);
}

