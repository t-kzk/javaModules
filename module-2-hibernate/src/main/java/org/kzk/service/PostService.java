package org.kzk.service;

import org.kzk.model.Label;
import org.kzk.model.Post;
import org.kzk.model.PostStatus;
import org.kzk.model.Writer;
import org.kzk.repository.PostRepository;
import org.kzk.repository.imp.PostRepositoryHibernateImpl;

import java.util.List;
import java.util.Optional;

public class PostService {
    private final PostRepository postRepository;
    private final LabelService labelService;
    private final WriterService writerService;

    public PostService() {
        this.postRepository = new PostRepositoryHibernateImpl();
        this.labelService = new LabelService();
        this.writerService = new WriterService();
    }
    public PostService(PostRepository postRepository, LabelService labelService, WriterService writerService) {
        this.postRepository = postRepository;
        this.labelService = labelService;
        this.writerService = writerService;
    }

    public Post createPost(
            Integer writerId,
            String content,
            List<Integer> labelIds) {

        List<Label> allLabels = labelService.findAllLabels().stream().filter(l -> labelIds.contains(l.getId())).toList();

        PostStatus postStatus;
        if (isContentValid(content)) {
            postStatus = PostStatus.ACTIVE;
        } else {
            postStatus = PostStatus.UNDER_REVIEW;
        }

        Writer writer = writerService.writerInfo(writerId);
        return postRepository.save(new Post(
                null,
                content,
                null,
                null,
                allLabels,
                postStatus,
                writer
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
                            post.getId(),
                            newContent,
                            null,
                            null,
                            post.getLabels(),
                            postStatus,
                            post.getWriter()));
        } else {
            throw new RuntimeException("no post id present [%d]".formatted(id));
        }

    }

    public boolean userDeletesPost(Integer uuidPost) {

        Post post = checkExistingPost(uuidPost);
        postRepository.update(
                new Post(
                        post.getId(),
                        post.getContent(),
                        null,
                        null,
                        post.getLabels(),
                        PostStatus.DELETED,
                        post.getWriter()));
        return true;

    }

    public boolean adminDeletesPost(Integer postId) {
        Post post = checkExistingPost(postId);
        return postRepository.delete(post);
    }

    public void adminReviewPost(Integer postId, PostStatus status) {
        Post post = checkExistingPost(postId);
        postRepository.update(new Post(
                post.getId(),
                post.getContent(),
                null,
                null,
                post.getLabels(),
                status,
                post.getWriter()
        ));
    }

     boolean isContentValid(String content) {
        if (content == null || content.isEmpty()) {
            return false;
        }

        if (content.contains("*")) {
            return false;
        }

        return true;
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
