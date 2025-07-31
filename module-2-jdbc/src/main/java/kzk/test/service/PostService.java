package kzk.test.service;

import kzk.test.model.Label;
import kzk.test.model.Post;
import kzk.test.model.PostStatus;
import kzk.test.repository.LabelRepository;
import kzk.test.repository.PostRepository;

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

    public Integer createPost(
            Integer writerId,
            String content,
            List<Integer> labelIds) {

        List<Label> allLabels = labelRepository.findAll().stream().filter(l->labelIds.contains(l.id())).toList();

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

    public boolean updatePostContent(Integer id, String newContent) {
        PostStatus postStatus;

        if (isContentValid(newContent)) {
            postStatus = PostStatus.ACTIVE;
        } else {
            postStatus = PostStatus.UNDER_REVIEW;
        }

        Optional<Post> byId = postRepository.findById(id);
        byId.ifPresent(e -> System.out.println("Найден! " + id.toString()));

        System.out.println("Все существуюшие");
        postRepository.findAll().forEach(e -> System.out.println(e.id()));

        return postRepository.updatePostContentById(id, newContent, postStatus) > 0;
    }

    public void userDeletesPost(Integer uuidPost) {
        postRepository.setStatusById(uuidPost, PostStatus.DELETED);
    }

    public void adminDeletesPost(Integer uuid) {
        postRepository.deleteById(uuid);
    }

    public void adminReviewPost(Integer post, PostStatus status) {
        postRepository.setStatusById(post, status);
    }

    private boolean isContentValid(String content) {
        return (content.isBlank() || content.contains("*"));
    }
}
