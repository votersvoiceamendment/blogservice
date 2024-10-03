package com.vva.blogservice.comments;

import com.vva.blogservice.posts.Post;
import com.vva.blogservice.posts.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class CommentIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private Long postId;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        // Create and save a post to associate comments
        Post post = new Post("aec1cc50-8b65-44e6-8ad8-9126e6916b07", "Post 1", "This is post 1");
        postRepository.save(post);
        postId = post.getId();
    }

    @Test
    public void canAddCommentToPost() throws Exception {
        String newCommentJson = """
            {
                "vvaUserId": "vva-user-id",
                "vvaUserName": "UserName",
                "text": "New comment text"
            }
        """;

        mockMvc.perform(post("/api/v1/posts/{postId}/comment", postId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newCommentJson))
                .andExpect(status().isOk());

        assert commentRepository.findAll().size() == 1;
    }

    @Test
    public void canGetCommentsForPost() throws Exception {
        Comment comment1 = new Comment("vva-user-id", "UserName", "Comment 1");
        Comment comment2 = new Comment("vva-user-id", "UserName", "Comment 2");

        comment1.setPost(postRepository.findById(postId).orElseThrow());
        comment2.setPost(postRepository.findById(postId).orElseThrow());

        commentRepository.save(comment1);
        commentRepository.save(comment2);

        mockMvc.perform(get("/api/v1/posts/{postId}/comment", postId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].text").value("Comment 1"))
                .andExpect(jsonPath("$[1].text").value("Comment 2"));
    }
}
