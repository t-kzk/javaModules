package org.kzk.repository;

import org.kzk.model.Post;
import org.kzk.model.PostStatus;

import java.util.List;

public interface PostRepository extends GenericRepository<Post, Integer> {

    List<Post> findAllByWriterId(Integer writerId);


}

