package com.vva.blogservice.comments;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    // DO NOT NEED THIS!
    // @Query("SELECT * FROM comment WHERE post_id = ? ORDER BY created_at DESC")
    // Spring boot can do it on its own
    List<Comment> findByPostIdOrderByCreatedAtDesc(Long postId);
}
