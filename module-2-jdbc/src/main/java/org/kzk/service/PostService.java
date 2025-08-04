package org.kzk.service;

import org.kzk.model.Label;
import org.kzk.model.Post;
import org.kzk.model.PostStatus;
import org.kzk.repository.LabelRepository;
import org.kzk.repository.PostRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PostService {
    private final PostRepository postRepository;
    private final LabelRepository labelRepository;

    public PostService(PostRepository postRepository, LabelRepository labelRepository) {
        this.postRepository = postRepository;
        this.labelRepository = labelRepository;
    }

    public Post createPost(
            Integer writerId,
            String content,
            List<Integer> labelIds) {

        // todo заменить на лейбл сервис
        List<Label> allLabels = labelRepository.findAll().stream().filter(l -> labelIds.contains(l.id())).toList();

        LocalDateTime created = LocalDateTime.now();
        PostStatus postStatus;
        if (isContentValid(content)) {
            postStatus = PostStatus.ACTIVE;
        } else {
            postStatus = PostStatus.UNDER_REVIEW;
        }
        return postRepository.save(new Post(
                null,
                content,
                created,
                null,
                allLabels, //todo add label
                postStatus,
                writerId
        ));
    }

    public Post updatePostContent(Integer id, String newContent) {
        PostStatus postStatus;

        if (isContentValid(newContent)) {
            postStatus = PostStatus.ACTIVE;
        } else {
            postStatus = PostStatus.UNDER_REVIEW;
        }

        Optional<Post> byId = postRepository.findById(id);
        if (byId.isPresent()) {
            Post post = byId.get();
            return postRepository.update(
                    new Post(
                            post.id(),
                            newContent,
                            null,
                            null,
                            post.labels(),
                            postStatus,
                            post.writerId()));
        } else {
            throw new RuntimeException("no post id present [%d]".formatted(id));
        }

    }

    public boolean userDeletesPost(Integer uuidPost) {

        Post post = checkExistingPost(uuidPost);
        postRepository.update(
                new Post(
                        post.id(),
                        post.content(),
                        null,
                        null,
                        post.labels(),
                        PostStatus.DELETED,
                        post.writerId()));
        return true;

    }

    public boolean adminDeletesPost(Integer postId) {
        Post post = checkExistingPost(postId);
        return postRepository.delete(post);
    }

    public void adminReviewPost(Integer postId, PostStatus status) {
        Post post = checkExistingPost(postId);
        postRepository.update(new Post(
                post.id(),
                post.content(),
                null,
                null,
                post.labels(),
                status,
                post.writerId()
        ));
    }

    private boolean isContentValid(String content) {
        return !(content.isBlank() || content.contains("*"));
    }

    public Post checkExistingPost(Integer idPost) {
        Optional<Post> byId = postRepository.findById(idPost);
        if (byId.isPresent()) {
            return byId.get();
        } else {
            throw new RuntimeException("no post id present [%d]".formatted(idPost));
        }
    }
}
