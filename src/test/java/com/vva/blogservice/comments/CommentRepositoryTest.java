package com.vva.blogservice.comments;

import com.vva.blogservice.posts.Post;
import com.vva.blogservice.posts.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import jakarta.transaction.Transactional;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DataJpaTest
@Transactional
@Rollback(value = true)
public class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    private Long postId;

    @BeforeEach
    void setUp() {
        Post post = new Post("aec1cc50-8b65-44e6-8ad8-9126e6916b07", "Post for comments", "This post will hold comments");
        postRepository.save(post);
        postId = post.getId();

        Comment comment1 = new Comment(post, "vva-user-id", "UserName1", "Comment 1");
        Comment comment2 = new Comment(post, "vva-user-id", "UserName2", "Comment 2");

        commentRepository.save(comment1);
        commentRepository.save(comment2);
    }

    // Test for finding comments by postId
    @Test
    void canFindCommentsByPostId() {
        List<Comment> comments = commentRepository.findByPostIdOrderByCreatedAtDesc(postId);

        assertThat(comments).hasSize(2);
        assertThat(comments.get(0).getText()).isEqualTo("Comment 2"); // Order by createdAt descending
        assertThat(comments.get(1).getText()).isEqualTo("Comment 1");
    }
}
