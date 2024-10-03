package com.vva.blogservice.comments;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class CommentValidationTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldFailValidationWhenVvaUserIdIsNullOrInvalid() {
        Comment invalidComment = new Comment(null, "UserName", "This is a valid comment text");
        Set<ConstraintViolation<Comment>> violations = validator.validate(invalidComment);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("vvaUserId for comment cannot be null or empty");
    }

    @Test
    void shouldFailValidationWhenCommentTextExceedsMaxLength() {
        String longText = "a".repeat(501); // Exceeds 500 characters
        Comment invalidComment = new Comment("vva-user-id", "UserName", longText);
        Set<ConstraintViolation<Comment>> violations = validator.validate(invalidComment);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Comment text cannot be more than 500 characters");
    }
}
