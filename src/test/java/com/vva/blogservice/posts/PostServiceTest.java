package com.vva.blogservice.posts;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PostServiceTest {

    private AutoCloseable closeable;

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostService postService;

    @BeforeEach
    void setup() {
        this.closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        // Close mocks to clean up the resources
        closeable.close();
    }

    @Test
    void canGetPosts () {
        this.postService.getPosts();
        verify(this.postRepository).findAllPostsOrderByFeaturedAndId();
    }

    @Test
    void getPostSuccess() {
        // Arrange
        Long postId = 1L;
        Post mockPost = new Post(
                postId,
                "vva-user-id",
                "MockTitle",
                "MockText",
                false,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        when(postRepository.findById(postId)).thenReturn(Optional.of(mockPost));

        // Act
        Post result = postService.getPost(postId);

        // Assert
        verify(postRepository).findById(postId); // Verify the repository method was called
        assertEquals(mockPost, result); // Ensure the returned post is the mock post
    }

    @Test
    void throwExceptionWhenPostDoesNotExist() {
        // Arrange
        Long postId = 1L;
        when(postRepository.findById(postId)).thenReturn(Optional.empty()); // Simulate post not found

        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> postService.getPost(postId));

        // Verify the exception message
        assertEquals("Post with the id " + postId + " does not exist.", exception.getMessage());

        // Verify the repository method was called
        verify(postRepository).findById(postId);
    }

    // Test for getPostIds()
    @Test
    void canGetPostIds() {
        // Arrange
        List<Long> mockIds = Arrays.asList(1L, 2L, 3L);
        when(postRepository.findAllPostIds()).thenReturn(mockIds);

        // Act
        List<Long> result = postService.getPostIds();

        // Assert
        verify(postRepository).findAllPostIds(); // Verify the repository method was called
        assertEquals(mockIds, result); // Ensure the returned list matches the mock data
    }

    // Test for createNewPost()
    @Test
    void canCreateNewPost() {
        // Arrange
        Post newPost = new Post(
                "vva-user-id",
                "New Post Title",
                "New Post Text",
                false
        );

        // Act
        postService.createNewPost(newPost);

        // Assert
        verify(postRepository).save(newPost); // Verify that the repository's save method was called
    }

    // Test for updatePost()
    @Test
    void canUpdatePost() {
        // Arrange
        Long postId = 1L;
        Post existingPost = new Post(
                postId,
                "vva-user-id",
                "Old Title",
                "Old Text",
                false,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        Post updatedPost = new Post(
                "vva-user-id",
                "Updated Title",
                "Updated Text",
                true
        );

        when(postRepository.findById(postId)).thenReturn(Optional.of(existingPost));

        // Act
        postService.updatePost(postId, updatedPost);

        // Assert
        verify(postRepository).findById(postId); // Verify findById was called
        // Ensure the post was updated correctly
        assertEquals("Updated Title", existingPost.getTitle());
        assertEquals("Updated Text", existingPost.getText());
        assertEquals(true, existingPost.isFeatured());
    }

    // Test for deletePost()
    @Test
    void canDeletePost() {
        // Arrange
        Long postId = 1L;
        when(postRepository.existsById(postId)).thenReturn(true);

        // Act
        postService.deletePost(postId);

        // Assert
        verify(postRepository).existsById(postId); // Verify existsById was called
        verify(postRepository).deleteById(postId); // Verify deleteById was called
    }

    // Test for deletePost when post does not exist
    @Test
    void throwExceptionWhenDeletingNonExistentPost() {
        // Arrange
        Long postId = 1L;
        when(postRepository.existsById(postId)).thenReturn(false);

        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> postService.deletePost(postId));

        // Verify the exception message
        assertEquals("A post with the id " + postId + " does not exist", exception.getMessage());

        // Verify existsById was called
        verify(postRepository).existsById(postId);
    }

}
