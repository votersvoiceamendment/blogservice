package com.vva.blogservice.post;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("SELECT p FROM Post p ORDER BY p.featured DESC, p.id DESC")
    List<Post> findAllPostsOrderByFeaturedAndId();

    // Custom query to return only post IDs
    @Query("SELECT p.id FROM Post p")
    List<Long> findAllPostIds();
}
