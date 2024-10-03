package com.vva.blogservice.comments;

import com.vva.blogservice.posts.Post;
import com.vva.blogservice.posts.PostRepository;
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

public class CommentServiceTest {

    private AutoCloseable closeable;

    @Mock
    private PostRepository postRepository;

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private CommentService commentService;

    @BeforeEach
    void setUp() {
        this.closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        // Close mocks to clean up the resources
        closeable.close();
    }

    // Test for getComments() when the post exists
    @Test
    void canGetCommentsForExistingPost() {
        // Arrange
        Long postId = 1L;
        when(postRepository.existsById(postId)).thenReturn(true);
        List<Comment> mockComments = Arrays.asList(
                new Comment(1L, new Post(), "vva-user-1", "User 1", "Comment 1", LocalDateTime.now(), LocalDateTime.now()),
                new Comment(2L, new Post(), "vva-user-2", "User 2", "Comment 2", LocalDateTime.now(), LocalDateTime.now())
        );
        when(commentRepository.findByPostIdOrderByCreatedAtDesc(postId)).thenReturn(mockComments);

        // Act
        List<Comment> result = commentService.getComments(postId);

        // Assert
        verify(postRepository).existsById(postId); // Verify post existence check
        verify(commentRepository).findByPostIdOrderByCreatedAtDesc(postId); // Verify comment fetch
        assertEquals(mockComments, result); // Ensure the returned comments are correct
    }

    // Test for getComments() when the post does not exist
    @Test
    void throwsExceptionWhenGettingCommentsForNonExistentPost() {
        // Arrange
        Long postId = 1L;
        when(postRepository.existsById(postId)).thenReturn(false);

        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> commentService.getComments(postId));

        // Verify exception message
        assertEquals("A post with the id " + postId + " does not exist, and so no comments can be returned.", exception.getMessage());

        verify(postRepository).existsById(postId); // Verify post existence check
    }

    // Test for addComment() when the post exists
    @Test
    void canAddCommentToExistingPost() {
        // Arrange
        Long postId = 1L;
        Post mockPost = new Post(postId, "vva-user-1", "Post Title", "Post Text", false, LocalDateTime.now(), LocalDateTime.now());
        Comment newComment = new Comment("vva-user-id", "UserName", "New Comment");

        when(postRepository.findById(postId)).thenReturn(Optional.of(mockPost));

        // Act
        commentService.addComment(postId, newComment);

        // Assert
        verify(postRepository).findById(postId); // Verify post fetch
        assertEquals(mockPost, newComment.getPost()); // Ensure the post is set on the comment
        verify(commentRepository).save(newComment); // Verify comment save
    }

    // Test for addComment() when the post does not exist
    @Test
    void throwsExceptionWhenAddingCommentToNonExistentPost() {
        // Arrange
        Long postId = 1L;
        Comment newComment = new Comment("vva-user-id", "UserName", "New Comment");
        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> commentService.addComment(postId, newComment));

        // Verify exception message
        assertEquals("A post with the id " + postId + " does not exist, so you cannot add a comment to it.", exception.getMessage());

        verify(postRepository).findById(postId); // Verify post fetch
    }
}

