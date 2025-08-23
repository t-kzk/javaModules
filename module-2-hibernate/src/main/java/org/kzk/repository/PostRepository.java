package org.kzk.repository;

import org.kzk.model.Post;

import java.util.List;

public interface PostRepository extends GenericRepository<Post, Integer> {

    List<Post> findAllByWriterId(Integer writerId);


}

