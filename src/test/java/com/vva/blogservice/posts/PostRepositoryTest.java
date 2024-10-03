package com.vva.blogservice.posts;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import jakarta.transaction.Transactional;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DataJpaTest
@Transactional
@Rollback(value = true)
public class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void setUp() {
        // Arrange - Create and save some posts for testing
        Post post1 = new Post(
                "aec1cc50-8b65-44e6-8ad8-9126e6916b07",
                "Title 1",
                "Content 1",
                false
        );
        // Featured post
        Post post2 = new Post(
                "aec1cc50-8b65-44e6-8ad8-9126e6918b07",
                "Title 2",
                "Content 2",
                true
        );
        Post post3 = new Post(
                "aec1cc50-8b65-44e6-8ad8-9126e6913b07",
                "Title 3",
                "Content 3",
                false
        );

        postRepository.save(post1);
        postRepository.save(post2); // Featured post
        postRepository.save(post3);
    }

    // Test for findAllPostsOrderByFeaturedAndId()
    @Test
    void canFindAllPostsOrderedByFeaturedAndId() {
        // Act
        List<Post> result = postRepository.findAllPostsOrderByFeaturedAndId();

        // Assert
        assertThat(result).hasSize(3);  // Check size
        assertThat(result.get(0).isFeatured()).isTrue();  // First post should be the featured one
        assertThat(result.get(0).getTitle()).isEqualTo("Title 2");  // Featured post has Title 2
        assertThat(result.get(1).getId()).isGreaterThan(result.get(2).getId());  // IDs in descending order
    }

    // Test for findAllPostIds()
    @Test
    void canFindAllPostIds() {
        // Act
        List<Long> postIds = postRepository.findAllPostIds();

        // Assert
        assertThat(postIds).hasSize(3);  // Check size
        assertThat(postIds.get(0)).isNotNull();  // Ensure IDs are not null
        assertThat(postIds.get(1)).isGreaterThan(0L);  // Ensure valid ID
    }
}
