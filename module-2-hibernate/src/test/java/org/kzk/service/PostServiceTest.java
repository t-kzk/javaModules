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
import org.kzk.jpa.converter.Updated;
import org.kzk.model.Post;
import org.kzk.model.PostStatus;
import org.kzk.model.Writer;
import org.kzk.repository.PostRepository;
import org.kzk.service.LabelService;
import org.kzk.service.PostService;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
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
    private WriterService writerService;

    @Mock
    private LabelService labelService;

    @InjectMocks
    private PostService postService = new PostService();

    private static Writer anyWriter = Writer.builder()
            .id(1)
            .build();

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        setField(postService, "postRepository", postRepository);
        setField(postService, "labelService", labelService);
        setField(postService, "writerService", writerService);

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
                        new Updated(LocalDate.now()),
                        new HashSet<>(),
                        PostStatus.ACTIVE,
                        anyWriter
                )),
                Arguments.of(new Post(
                        1,
                        "co*tent",
                        LocalDateTime.now(),
                        new Updated(LocalDate.now()),
                        new HashSet<>(),
                        PostStatus.UNDER_REVIEW,
                        anyWriter
                ))
        );
    }

    @ParameterizedTest
    @MethodSource("provideCreatePosts")
    @DisplayName("Метод сохранения поста")
    void createPostTest(Post post) {
        // Arrange
        when(labelService.findAllLabelsByIds(any())).thenReturn(new HashSet<>());
        when(postRepository.save(new Post(
                null,
                post.getContent(),
                any(),
                null,
                new HashSet<>(),
                post.getStatus(),
                post.getWriter()
        ))).thenReturn(post);
        when(writerService.writerInfo(post.getWriter().getId())).thenReturn(post.getWriter());
        // Act
        Post postActual = postService.createPost(
                post.getWriter().getId(), post.getContent(), new HashSet<>());
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
                new Updated(LocalDate.now()),
                new HashSet<>(),
                PostStatus.ACTIVE,
                anyWriter
        );

        Post post = new Post(
                oldPost.getId(),
                "newContent",
                null,
                null,
                oldPost.getLabels(),
                PostStatus.ACTIVE,
                oldPost.getWriter()
        );
        when(postRepository.findById(any())).thenReturn(Optional.ofNullable(oldPost));
        when(postRepository.update(any(Post.class))).thenReturn(post);
        // Act
        Post result = postService.updatePostContent(1, post.getContent());

        assertEquals(post, result);
    }

    @Test
    void userDeletePost() {
        // Arrange
        Post post = new Post(
                1,
                "content",
                LocalDateTime.now(),
                new Updated(LocalDate.now()),
                new HashSet<>(),
                PostStatus.UNDER_REVIEW,
                anyWriter
        );
        when(postRepository.findById(any())).thenReturn(Optional.ofNullable(post));
        ArgumentCaptor<Post> postCaptor = ArgumentCaptor.forClass(Post.class);
        when(postRepository.update(postCaptor.capture())).thenReturn(any());

        // Act
        postService.userDeletesPost(1);

        // Assert
        Post result = postCaptor.getValue();
        assertEquals(PostStatus.DELETED, result.getStatus());

    }

    @Test
    void adminDeletePost() {
        // Arrange
        Post post = new Post(
                1,
                "content",
                LocalDateTime.now(),
                new Updated(LocalDate.now()),
                new HashSet<>(),
                PostStatus.UNDER_REVIEW,
                anyWriter
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
                new Updated(LocalDate.now()),
                new HashSet<>(),
                PostStatus.UNDER_REVIEW,
                anyWriter
        );
        when(postRepository.findById(any())).thenReturn(Optional.ofNullable(post));
        ArgumentCaptor<Post> postCaptor = ArgumentCaptor.forClass(Post.class);
        when(postRepository.update(postCaptor.capture())).thenReturn(any());

        // Act
        postService.adminReviewPost(1, newStatus);

        // Assert
        Post result = postCaptor.getValue();
        assertEquals(newStatus, result.getStatus());
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
                new Updated(LocalDate.now()),
                new HashSet<>(),
                PostStatus.UNDER_REVIEW,
                anyWriter
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