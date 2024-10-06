package com.vva.blogservice.posts;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping(path = "api/v1/posts")
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

    @GetMapping(path = "{postId}")
    public Post getPost(@PathVariable("postId") Long postId) {
        return this.postService.getPost(postId);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping
    public void createNewPost(@Valid @RequestBody Post post) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String userId = jwt.getSubject();
        post.setVvaUserId(userId);
/*
         Extract roles from JWT for logging/debugging (optional)
        String roles = jwt.getClaimAsString("roles");
        System.out.println("Roles: " + roles);
        Collection<GrantedAuthority> authorities = (Collection<GrantedAuthority>) authentication.getAuthorities();
        authorities.forEach(authority -> {
            System.out.println("Roasdasdfle: " + authority.getAuthority());
        });
        System.out.println("JWT Claims: " + jwt.getClaims());  // To check all JWT claims
        System.out.println("Authorities: " + authorities);
        System.out.println(post);
*/
        this.postService.createNewPost(post);
    }

//    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping(path = "{postId}")
    public void updatePost(@PathVariable("postId") Long postId, @Valid @RequestBody Post updatedPost) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String userId = jwt.getSubject();
        updatedPost.setVvaUserId(userId);
        this.postService.updatePost(postId, updatedPost);
    }

//    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping(path = "{postId}")
    public void deletePost(@PathVariable("postId") Long postId) {
        this.postService.deletePost(postId);
    }
}
