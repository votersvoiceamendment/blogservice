package com.vva.blogservice.posts;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class PostValidationTest {

    private static Validator validator;

    @BeforeAll
    public static void setUpValidator() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    void whenPostIsValid_thenNoViolations() {
        // Arrange
        Post validPost = new Post("aec1cc50-8b65-44e6-8ad8-9126e6916b07", "Valid Title", "Valid Text");

        // Act
        Set<ConstraintViolation<Post>> violations = validator.validate(validPost);

        // Assert
        assertThat(violations).isEmpty();
    }

    @Test
    void whenVvaUserIdIsInvalid_thenViolation() {
        // Arrange: vvaUserId should be exactly 36 characters
        Post invalidPost = new Post("short-vva-user-id", "Valid Title", "Valid Text");

        // Act
        Set<ConstraintViolation<Post>> violations = validator.validate(invalidPost);

        // Assert
        assertThat(violations).hasSize(1);
        ConstraintViolation<Post> violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("vvaUserId for post must be 36 characters");
    }

    @Test
    void whenTitleIsBlank_thenViolation() {
        // Arrange: title is blank
        Post invalidPost = new Post("aec1cc50-8b65-44e6-8ad8-9126e6916b07", "", "Valid Text");

        // Act
        Set<ConstraintViolation<Post>> violations = validator.validate(invalidPost);

        // Assert
        assertThat(violations).hasSize(1);
        ConstraintViolation<Post> violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("Post title cannot be null or empty");
    }

    @Test
    void whenTitleExceedsMaxLength_thenViolation() {
        // Arrange: title exceeds 500 characters
        String longTitle = "a".repeat(501);
        Post invalidPost = new Post("aec1cc50-8b65-44e6-8ad8-9126e6916b07", longTitle, "Valid Text");

        // Act
        Set<ConstraintViolation<Post>> violations = validator.validate(invalidPost);

        // Assert
        assertThat(violations).hasSize(1);
        ConstraintViolation<Post> violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("Post title cannot be more than 500 characters");
    }

    @Test
    void whenTextIsBlank_thenViolation() {
        // Arrange: text is blank
        Post invalidPost = new Post("aec1cc50-8b65-44e6-8ad8-9126e6916b07", "Valid Title", "");

        // Act
        Set<ConstraintViolation<Post>> violations = validator.validate(invalidPost);

        // Assert
        assertThat(violations).hasSize(1);
        ConstraintViolation<Post> violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("Post text cannot be null or empty");
    }

    @Test
    void whenTextExceedsMaxLength_thenViolation() {
        // Arrange: text exceeds 20000 characters
        String longText = "a".repeat(20001);
        Post invalidPost = new Post("aec1cc50-8b65-44e6-8ad8-9126e6916b07", "Valid Title", longText);

        // Act
        Set<ConstraintViolation<Post>> violations = validator.validate(invalidPost);

        // Assert
        assertThat(violations).hasSize(1);
        ConstraintViolation<Post> violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("Post text cannot be more than 20000 characters");
    }
}
