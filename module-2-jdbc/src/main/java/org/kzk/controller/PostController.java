package org.kzk.controller;

import org.kzk.model.Post;
import org.kzk.repository.LabelRepository;
import org.kzk.repository.PostRepository;
import org.kzk.service.PostService;

import java.util.List;

public class PostController {
    private final PostService postService;

    public PostController() {
        this.postService = new PostService();
    }

    public Post createPost(
            Integer writerId,
            String content,
            List<Integer> labelIds) {
        return postService.createPost(writerId, content, labelIds);
    }

    public Post updatePostContent(Integer postId, String newContent) {
       return postService.updatePostContent(postId, newContent);
    }

    public void userDeletesPost(Integer postId) {
        postService.userDeletesPost(postId);
    }
}
