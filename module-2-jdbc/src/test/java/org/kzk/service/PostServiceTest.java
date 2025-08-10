package org.kzk.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.kzk.model.Post;
import org.kzk.model.PostStatus;
import org.kzk.repository.PostRepository;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private LabelService labelService;

    @InjectMocks
    private  PostService postService = new PostService();

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        setField(postService, "postRepository", postRepository);
        setField(postService, "labelService", labelService);

    }

    private void setField(Object target, String fieldName, Object value)
            throws NoSuchFieldException, IllegalAccessException {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    private static Stream<Arguments> provideCreatePosts() {
        return Stream.of(
                Arguments.of(new Post(
                        1,
                        "content",
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        new ArrayList<>(),
                        PostStatus.ACTIVE,
                        1
                )),
                Arguments.of(new Post(
                        1,
                        "co*tent",
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        new ArrayList<>(),
                        PostStatus.UNDER_REVIEW,
                        1
                ))
        );
    }

    @ParameterizedTest
    @MethodSource("provideCreatePosts")
    @DisplayName("Метод сохранения поста")
    void createPostTest(Post post) {
        // Arrange
        when(labelService.findAllLabels()).thenReturn(new ArrayList<>());
        when(postRepository.save(new Post(
                null,
                post.content(),
                null,
                null,
                new ArrayList<>(),
                post.status(),
                post.writerId()
        ))).thenReturn(post);
        // Act
        Post postActual = postService.createPost(post.writerId(), post.content(), new ArrayList<>());
        // Assert
        assertEquals(post, postActual);
    }

    @Test
    void updatePostTest() {
        // Arrange
        Post oldPost = new Post(
                1,
                "content",
                LocalDateTime.now(),
                LocalDateTime.now(),
                new ArrayList<>(),
                PostStatus.ACTIVE,
                1
        );

        Post post = new Post(
                oldPost.id(),
                "newContent",
                null,
                null,
                oldPost.labels(),
                PostStatus.ACTIVE,
                oldPost.writerId()
        );
        when(postRepository.findById(any())).thenReturn(Optional.ofNullable(oldPost));
        when(postRepository.update(any(Post.class))).thenReturn(post);
        // Act
        Post result = postService.updatePostContent(1, post.content());

        assertEquals(post, result);
    }

    @Test
    void userDeletePost() {
        // Arrange
        Post post = new Post(
                1,
                "content",
                LocalDateTime.now(),
                LocalDateTime.now(),
                new ArrayList<>(),
                PostStatus.UNDER_REVIEW,
                1
        );
        when(postRepository.findById(any())).thenReturn(Optional.ofNullable(post));
        ArgumentCaptor<Post> postCaptor = ArgumentCaptor.forClass(Post.class);
        when(postRepository.update(postCaptor.capture())).thenReturn(any());

        // Act
        postService.userDeletesPost(1);

        // Assert
        Post result = postCaptor.getValue();
        assertEquals(PostStatus.DELETED, result.status());

    }

    @Test
    void adminDeletePost() {
        // Arrange
        Post post = new Post(
                1,
                "content",
                LocalDateTime.now(),
                LocalDateTime.now(),
                new ArrayList<>(),
                PostStatus.UNDER_REVIEW,
                1
        );
        when(postRepository.findById(any())).thenReturn(Optional.ofNullable(post));
        ArgumentCaptor<Post> postCaptor = ArgumentCaptor.forClass(Post.class);
        when(postRepository.delete(postCaptor.capture())).thenReturn(true);

        // Act
        postService.adminDeletesPost(1);
        // Assert
        Post result = postCaptor.getValue();
        assertEquals(post, result);

    }

    @Test
    void adminReviewPostTest() {
        // Arrange
        PostStatus newStatus = PostStatus.ACTIVE;

        Post post = new Post(
                1,
                "content",
                LocalDateTime.now(),
                LocalDateTime.now(),
                new ArrayList<>(),
                PostStatus.UNDER_REVIEW,
                1
        );
        when(postRepository.findById(any())).thenReturn(Optional.ofNullable(post));
        ArgumentCaptor<Post> postCaptor = ArgumentCaptor.forClass(Post.class);
        when(postRepository.update(postCaptor.capture())).thenReturn(any());

        // Act
        postService.adminReviewPost(1, newStatus);

        // Assert
        Post result = postCaptor.getValue();
        assertEquals(newStatus, result.status());
    }

    @ParameterizedTest
    @CsvSource({
            ",false",
            "fu*k,false",
            "Hello World,true"})
    @DisplayName("Проверка метода первичного ревью")
    void isContentValidTest(String content, boolean expected) {
        // Act
        boolean result = postService.isContentValid(content);
        // Assert
        assertEquals(expected, result);

    }

    @Test
    @DisplayName("Метод проверки существующего поста по postId. postRepository вернул пост")
    void checkExistingPostTesRepoReturnPost() {
        // Arrange
        Post post = new Post(
                1,
                "content",
                LocalDateTime.now(),
                LocalDateTime.now(),
                new ArrayList<>(),
                PostStatus.UNDER_REVIEW,
                1
        );
        when(postRepository.findById(any())).thenReturn(Optional.ofNullable(post));
        // Act
        Post result = postService.checkExistingPost(1);
        // Assert
        assertEquals(post, result);

    }

    @Test
    @DisplayName("Метод проверки существующего поста. postRepository вернул null")
    void checkExistingPostTesRepoReturnNull() {
        // Arrange
        when(postRepository.findById(any())).thenReturn(Optional.ofNullable(null));
        // Act // Assert
        Assertions.assertThrows(RuntimeException.class, () -> postService.checkExistingPost(1));
    }


}