package org.kzk.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.kzk.model.Post;
import org.kzk.model.PostStatus;
import org.kzk.repository.PostRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class) // Добавьте эту аннотацию

class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    private final PostService postService = new PostService();

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        setField(postService, "postRepository", postRepository);

    }
    private void setField(Object target, String fieldName, Object value)
            throws NoSuchFieldException, IllegalAccessException {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
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
    @DisplayName("123")
    void checkExistingPostTes2() {
        // Arrange
        String content = "content";
        Post post = new Post(
                1,
                "content",
                LocalDateTime.now(),
                LocalDateTime.now(),
                new ArrayList<>(),
                PostStatus.UNDER_REVIEW,
                1
        );
        Mockito.when(postRepository.findById(any())).thenReturn(Optional.ofNullable(post));
        // Act
        Post result = postService.checkExistingPost(1);

        // Assert
        assertEquals(post, result);

    }

}