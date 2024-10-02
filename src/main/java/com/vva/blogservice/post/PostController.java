package com.vva.blogservice.post;


import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path="api/v1/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public List<Post> getPosts() {
        return this.postService.getPosts();
    }

    @GetMapping(path = "/ids")
    public List<Long> getPostIds() {
        return this.postService.getPostIds();
    }

    @GetMapping(path = "{id}")
    public Post getPost(@PathVariable("id") Long id) {
        return this.postService.getPost(id);
    }

    @PostMapping
    public void createNewPost(@RequestBody Post post) {
        this.postService.createNewPost(post);
    }

    @PutMapping(path = "{id}")
    public void updatePost(@PathVariable("id") Long id, @RequestBody Post updatedPost) {
        this.postService.updatePost(id, updatedPost);
    }

    @DeleteMapping(path = "{id}")
    public void deletePost(@PathVariable("id") Long id) {
        this.postService.deletePost(id);
    }
}
