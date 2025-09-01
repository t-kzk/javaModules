package org.kzk.controller;

import org.kzk.model.Post;
import org.kzk.service.PostService;

import java.util.List;
import java.util.Set;

public class PostController {
    private final PostService postService;

    public PostController() {
        this.postService = new PostService();
    }

    public Post createPost(
            Integer writerId,
            String content,
            Set<Integer> labelIds) {
        return postService.createPost(writerId, content, labelIds);
    }

    public Post updatePostContent(Integer postId, String newContent) {
       return postService.updatePostContent(postId, newContent);
    }

    public void userDeletesPost(Integer postId) {
        postService.userDeletesPost(postId);
    }
}
