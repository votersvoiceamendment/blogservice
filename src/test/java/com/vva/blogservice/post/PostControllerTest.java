package com.vva.blogservice.post;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.vva.blogservice.posts.Post;
import com.vva.blogservice.posts.PostController;
import com.vva.blogservice.posts.PostService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PostController.class)
public class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PostService postService;

    @Test
    void canGetPosts() throws Exception {
        mockMvc.perform(get("/api/v1/posts"))
                .andExpect(status().isOk());
    }

    @Test
    void canGetPostIds() throws Exception {
        mockMvc.perform(get("/api/v1/posts/ids"))
                .andExpect((status().isOk()));
    }

    @Test
    void canGetPost() throws Exception {
        mockMvc.perform(get("/api/v1/posts/{id}", 1L))
                .andExpect(status().isOk());
    }

    @Test
    void canCreatePostWithFeatured() throws Exception {
        Post fakePost = new Post(
                "aec1cc50-8b65-44e6-8ad8-9126e6916b07",
                "Fake Title",
                "Fake Text",
                false
        );

        mockMvc.perform(post("/api/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(fakePost)))
                .andExpect(status().isOk());
    }

    @Test
    void canCreatePostNoFeatured() throws Exception {
        Post fakePost = new Post(
                "aec1cc50-8b65-44e6-8ad8-9126e6916b07",
                "Fake Title",
                "Fake Text"
        );

        mockMvc.perform(post("/api/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(fakePost)))
                .andExpect(status().isOk());
    }

    @Test
    void canUpdatePostWithFeatured() throws Exception {
        Post fakePost = new Post(
                "aec1cc50-8b65-44e6-8ad8-9126e6916b07",
                "Fake Title",
                "Fake Text",
                true
        );

        mockMvc.perform(put("/api/v1/posts/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(fakePost)))
                .andExpect(status().isOk());
    }

    @Test
    void canUpdatePostNoFeatured() throws Exception {
        Post fakePost = new Post(
                "aec1cc50-8b65-44e6-8ad8-9126e6916b07",
                "Fake Title",
                "Fake Text"
        );

        mockMvc.perform(put("/api/v1/posts/{id}", 1L)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsString(fakePost)))
                .andExpect(status().isOk());
    }

    @Test
    void canDeletePost() throws Exception {
        mockMvc.perform(delete("/api/v1/posts/{id}", 1L))
                .andExpect(status().isOk());
    }

    // Tests for when errors occur

    // Testing 404 Not Found for Non-Existent Post (GET)
    @Test
    void getNonExistentPostReturns404() throws Exception {
        // Mock PostService to throw IllegalStateException when the post doesn't exist
        doThrow(new IllegalStateException("Post with the id 999 does not exist."))
                .when(postService).getPost(999L);

        mockMvc.perform(get("/api/v1/posts/{id}", 999L))
                .andExpect(status().isNotFound());
    }

    // Testing 404 Not Found for Non-Existent Post (DELETE)
    @Test
    void deleteNonExistentPostReturns404() throws Exception {
        // Mock PostService to throw IllegalStateException when trying to delete a non-existent post
        doThrow(new IllegalStateException("A post with the id 999 does not exist"))
                .when(postService).deletePost(999L);

        mockMvc.perform(delete("/api/v1/posts/{id}", 999L))
                .andExpect(status().isNotFound());
    }

    // Testing 400 Bad Request for Invalid Data (POST)
    @Test
    void createPostWithMissingTitleReturns400() throws Exception {
        // Create a post object with missing title
        Post invalidPost = new Post(null, "Fake Text", "aec1cc50-8b65-44e6-8ad8-9126e6916b07");

        mockMvc.perform(post("/api/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(invalidPost)))
                .andExpect(status().isBadRequest());
    }


    // Testing 400 Bad Request for Invalid Data (PUT)
    @Test
    void updatePostWithInvalidDataReturns400() throws Exception {
        // Create an invalid post object with missing text
        Post invalidPost = new Post("Fake Title", null, "aec1cc50-8b65-44e6-8ad8-9126e6916b07");

        mockMvc.perform(put("/api/v1/posts/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(invalidPost)))
                .andExpect(status().isBadRequest());
    }

    // Testing 400 Bad Request for Invalid ID Type (GET)
    @Test
    void getPostWithInvalidIdTypeReturns400() throws Exception {
        mockMvc.perform(get("/api/v1/posts/{id}", "invalid_id"))
                .andExpect(status().isBadRequest());
    }

    // Testing 400 Bad Request for Invalid ID Type (DELETE)
    @Test
    void deletePostWithInvalidIdTypeReturns400() throws Exception {
        mockMvc.perform(delete("/api/v1/posts/{id}", "invalid_id"))
                .andExpect(status().isBadRequest());
    }

    // Test for Empty Input Data (POST)
    @Test
    void createPostWithEmptyBodyReturns400() throws Exception {
        mockMvc.perform(post("/api/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))  // Sending empty JSON
                .andExpect(status().isBadRequest());
    }

    // Test for Null Values in Critical Fields (PUT)
    @Test
    void updatePostWithNullCriticalFieldsReturns400() throws Exception {
        Post invalidPost = new Post(null, null, null);  // Invalid Post with all fields null

        mockMvc.perform(put("/api/v1/posts/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(invalidPost)))
                .andExpect(status().isBadRequest());
    }

//    // Test for Database Constraints (e.g., Duplicate Data)
//    @Test
//    void createPostWithDuplicateTitleReturns409() throws Exception {
//        Post duplicatePost = new Post(
//                "existing_title",
//                "Duplicate text",
//                "aec1cc50-8b65-44e6-8ad8-9126e6916b07"
//        );
//
//        // Simulate behavior where PostService throws an exception for duplicate title
//        doThrow(new IllegalStateException("Post with this title already exists"))
//                .when(postService).createNewPost(any(Post.class));
//
//        mockMvc.perform(post("/api/v1/posts")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(new ObjectMapper().writeValueAsString(duplicatePost)))
//                .andExpect(status().isConflict());  // 409 Conflict
//    }
//
//    // Test Unauthorized Access (if applicable)
//    @Test
//    void updatePostWithoutProperRoleReturns403() throws Exception {
//        Post fakePost = new Post("Fake Title", "Fake Text", "aec1cc50-8b65-44e6-8ad8-9126e6916b07");
//
//        mockMvc.perform(put("/api/v1/posts/{id}", 1L)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(new ObjectMapper().writeValueAsString(fakePost)))
//                .andExpect(status().isForbidden());  // Assuming RBAC prevents access
//    }

    // Test for too long a title (POST/PUT)
    @Test
    void createPostWithTooLongTitle400() throws Exception {
        // Simulate a very large post content
        String largeTitle = "a".repeat(501);

        Post largePost = new Post(
                "Large Post",
                largeTitle,
                "aec1cc50-8b65-44e6-8ad8-9126e6916b07"
        );

        mockMvc.perform(post("/api/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(largePost)))
                .andExpect(status().isBadRequest());
    }

    // Test for Invalid JSON Format (POST/PUT)
    @Test
    void createPostWithInvalidJsonReturns400() throws Exception {
        String invalidJson = "{ title: Fake Title, text: Fake Text";  // Improperly formatted JSON

        mockMvc.perform(post("/api/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());
    }
}
