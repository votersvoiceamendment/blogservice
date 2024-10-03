package com.vva.blogservice.comment;

import com.vva.blogservice.comments.Comment;
import com.vva.blogservice.comments.CommentController;
import com.vva.blogservice.comments.CommentService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommentController.class)
public class CommentControllerTest {

    private AutoCloseable closeable;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;

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
        Comment newComment = new Comment("aec1cc50-8b65-44e6-8ad8-9126e6916b07", "UserName", "New Comment");

        // Act & Assert
        mockMvc.perform(post("/api/v1/posts/{postId}/comment", postId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"vvaUserId\":\"aec1cc50-8b65-44e6-8ad8-9126e6916b07\", \"vvaUserName\":\"UserName\", \"text\":\"New Comment\"}"))
                .andExpect(status().isOk());

        verify(commentService).addComment(eq(postId), refEq(newComment));
    }
}

