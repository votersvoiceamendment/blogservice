package com.vva.blogservice.comment;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path="api/v1/post/{postId}/comment")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping
    public List<Comment> getComments(@PathVariable("postId") Long postId) {
        return this.commentService.getComments(postId);
    }

    @PostMapping
    public void addComment(@PathVariable("postId") Long postId, @RequestBody Comment newComment) {
        this.commentService.addComment(postId, newComment);
    }
}
