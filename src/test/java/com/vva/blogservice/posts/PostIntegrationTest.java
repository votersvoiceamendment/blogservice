package com.vva.blogservice.posts;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class PostIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    // Test for creating a new post
    @Test
    public void createNewPostTest() throws Exception {
        String newPostJson = """
            {
                "vvaUserId": "aec1cc50-8b65-44e6-8ad8-9126e6916b07",
                "title": "New Post Title",
                "text": "This is the post text",
                "featured": false
            }
        """;

        mockMvc.perform(post("/api/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newPostJson))
                .andExpect(status().isOk());

        // Assert that the post was created
        assert postRepository.findAll().size() == 1;
    }

    // Test for retrieving posts
    @Test
    public void getPostsTest() throws Exception {
        Post post1 = new Post("aec1cc50-8b65-44e6-8ad8-9126e6916b07", "Post 1", "This is post 1");
        Post post2 = new Post("aec1cc50-8b65-44e6-8ad8-9126e6916b07", "Post 2", "This is post 2");
        postRepository.save(post1);
        postRepository.save(post2);

        // The order when retreiving posts is
        //  -featured
        //  -Ids - descending
        mockMvc.perform(get("/api/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].title").value("Post 2"))
                .andExpect(jsonPath("$[1].title").value("Post 1"));
    }

    // Test for updating a post
    @Test
    public void updatePostTest() throws Exception {
        Post post = new Post("aec1cc50-8b65-44e6-8ad8-9126e6916b07", "Original Title", "Original Text");
        postRepository.save(post);

        String updatedPostJson = """
            {
                "vvaUserId": "aec1cc50-8b65-44e6-8ad8-9126e6916b07",
                "title": "Updated Title",
                "text": "Updated Text",
                "featured": true
            }
        """;

        mockMvc.perform(put("/api/v1/posts/{id}", post.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedPostJson))
                .andExpect(status().isOk());

        Post updatedPost = postRepository.findById(post.getId()).orElseThrow();
        assert updatedPost.getTitle().equals("Updated Title");
        assert updatedPost.getText().equals("Updated Text");
    }

    // Test for deleting a post
    @Test
    public void deletePostTest() throws Exception {
        Post post = new Post("aec1cc50-8b65-44e6-8ad8-9126e6916b07", "Post to delete", "This post will be deleted");
        postRepository.save(post);

        mockMvc.perform(delete("/api/v1/posts/{id}", post.getId()))
                .andExpect(status().isOk());

        assert postRepository.findById(post.getId()).isEmpty();
    }
}

