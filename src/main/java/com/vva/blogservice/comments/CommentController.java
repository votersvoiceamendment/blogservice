package com.vva.blogservice.comments;

import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path="api/v1/posts/{postId}/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping
    public List<Comment> getComments(@PathVariable("postId") Long postId) {
        return this.commentService.getComments(postId);
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PostMapping
    public void addComment(@PathVariable("postId") Long postId, @Valid @RequestBody Comment newComment) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String userId = jwt.getSubject();
        newComment.setVvaUserId((userId));
        this.commentService.addComment(postId, newComment);
    }
}
