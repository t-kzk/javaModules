package org.kzk.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PostServiceTest {
    private final PostService postService = new PostService();

    @Test
    @DisplayName("123")
    void isContentValidTest() {
       // Arrange
        String content = "content";

        // Act
        boolean result = postService.isContentValid(content);

        // Assert
        assertTrue(result);

    }
    @Test
    @DisplayName("123")
    void isContentValidTes2t() {
       // Arrange
        String content = "content";

        // Act
        boolean result = postService.isContentValid(content);

        // Assert
        assertTrue(result);

    }

}