package org.kzk.service;

import org.kzk.jpa.converter.Updated;
import org.kzk.model.Label;
import org.kzk.model.Post;
import org.kzk.model.PostStatus;
import org.kzk.model.Writer;
import org.kzk.repository.PostRepository;
import org.kzk.repository.imp.HibernatePostRepositoryImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

public class PostService {
    private final PostRepository postRepository;
    private final LabelService labelService;
    private final WriterService writerService;

    public PostService() {
        this.postRepository = new HibernatePostRepositoryImpl();
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
            Set<Integer> labelIds) {

        Set<Label> allLabels = labelService.findAllLabelsByIds(labelIds);
        if(labelIds.size()!=allLabels.size()) {
            System.out.println("oi");
            throw new IllegalArgumentException("Labels not exist " + labelIds);
        }

        PostStatus postStatus;
        if (isContentValid(content)) {
            postStatus = PostStatus.ACTIVE;
        } else {
            postStatus = PostStatus.UNDER_REVIEW;
        }

        Writer writer = writerService.writerInfo(writerId);

        Post post = Post.builder()
                .content(content)
                .created(LocalDateTime.now())
                .labels(allLabels)
                .status(postStatus)
                .writer(writer)
                .build();
        return postRepository.save(post);
    }

    public Post updatePostContent(Integer id, String newContent) {
        PostStatus postStatus;

        if (isContentValid(newContent)) {
            postStatus = PostStatus.ACTIVE;
        } else {
            postStatus = PostStatus.UNDER_REVIEW;
        }

        Post post = checkExistingPost(id);
        post.setContent(newContent);
        post.setUpdated(new Updated(LocalDate.now()));
        post.setStatus(postStatus);
        return postRepository.update(post);

    }

    public void userDeletesPost(Integer uuidPost) {
        Post post = checkExistingPost(uuidPost);
        post.setStatus(PostStatus.DELETED);
        postRepository.update(post);
    }

    public boolean adminDeletesPost(Integer postId) {
        Post post = checkExistingPost(postId);
        return postRepository.delete(post);
    }

    public void adminReviewPost(Integer postId, PostStatus status) {
        Post post = checkExistingPost(postId);
        post.setStatus(status);
        postRepository.update(post);
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
