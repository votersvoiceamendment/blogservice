package com.vva.blogservice.posts;

import com.vva.blogservice.utils.UpdateUtils;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public List<Post> getPosts() {
        return this.postRepository.findAllPostsOrderByFeaturedAndId();
    }

    public Post getPost(Long id) {
        return this.postRepository.findById(id).orElseThrow(() -> {
            return new IllegalStateException("Post with the id "+id+" does not exist.");
        });
    }

    public List<Long> getPostIds() {
        return this.postRepository.findAllPostIds();
    }

    public void createNewPost(Post post) {
        this.postRepository.save(post);
    }

    // You don't need to make an actual DB call because
    // @Transactional makes it so you can directly work
    // with the object to change things
    @Transactional
    public void updatePost(Long id, Post updatedPost) {
        Post post = this.postRepository.findById(id).orElseThrow(() -> {
            return new IllegalStateException("Post with the id "+id+" does not exist.");
        });

        // Change the values for the post
        // This uses a utils function that handles checking if a null values
        // was sent in the request and if a change has occurred
        UpdateUtils.updateFieldIfChanged(post::getTitle, post::setTitle, updatedPost.getTitle());
        UpdateUtils.updateFieldIfChanged(post::getText, post::setText, updatedPost.getText());
        UpdateUtils.updateFieldIfChanged(post::isFeatured, post::setFeatured, updatedPost.isFeatured());
    }

    public void deletePost(Long id) {
        boolean exists = this.postRepository.existsById(id);
        if (!exists) {
            throw new IllegalStateException("A post with the id "+id+" does not exist");
        }

        this.postRepository.deleteById(id);
    }
}
