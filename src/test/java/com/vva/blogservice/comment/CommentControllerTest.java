package com.vva.blogservice.comment;

import com.vva.blogservice.comment.Comment;
import com.vva.blogservice.comment.CommentService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CommentControllerTest {

    private AutoCloseable closeable;

    private MockMvc mockMvc;

    @Mock
    private CommentService commentService;

    @InjectMocks
    private CommentController commentController;

    @BeforeEach
    void setUp() {
        this.closeable = MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(commentController).build();
    }

    @AfterEach
    void tearDown() throws Exception {
        // Close mocks to clean up the resources
        closeable.close();
    }

    // Test for getComments() endpoint
    @Test
    void canGetCommentsForPost() throws Exception {
        // Arrange
        Long postId = 1L;
        List<Comment> mockComments = Arrays.asList(
                new Comment(1L, null, "vva-user-1", "User 1", "Comment 1", LocalDateTime.now(), LocalDateTime.now()),
                new Comment(2L, null, "vva-user-2", "User 2", "Comment 2", LocalDateTime.now(), LocalDateTime.now())
        );
        when(commentService.getComments(postId)).thenReturn(mockComments);

        // Act & Assert
        mockMvc.perform(get("/api/v1/posts/{postId}/comment", postId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(commentService).getComments(postId); // Verify service method was called
    }

    // Test for addComment() endpoint
    @Test
    void canAddCommentToPost() throws Exception {
        // Arrange
        Long postId = 1L;
        Comment newComment = new Comment("vva-user-id", "UserName", "New Comment");

        // Act & Assert
        mockMvc.perform(post("/api/v1/posts/{postId}/comment", postId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"vvaUserId\":\"vva-user-id\", \"vvaUserName\":\"UserName\", \"text\":\"New Comment\"}"))
                .andExpect(status().isOk());

        verify(commentService).addComment(postId, newComment); // Verify service method was called
    }
}

