package com.vva.blogservice.comments;

import com.vva.blogservice.posts.Post;
import com.vva.blogservice.posts.PostRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository, PostRepository postRepository) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;

    }

    public List<Comment> getComments(Long postId) {
        boolean postExists = this.postRepository.existsById(postId);
        if (!postExists) {
            throw new IllegalStateException("A post with the id "+postId+" does not exist, and so no comments can be returned.");
        }

        return this.commentRepository.findByPostIdOrderByCreatedAtDesc(postId);
    }

    public void addComment(Long postId, Comment newComment) {
        Post post = this.postRepository.findById(postId).orElseThrow(() -> {
           return new IllegalStateException("A post with the id " + postId + " does not exist, so you cannot add a comment to it.");
        });

        newComment.setPost(post);

        this.commentRepository.save(newComment);
    }
}
