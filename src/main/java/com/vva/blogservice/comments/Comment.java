package com.vva.blogservice.comments;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vva.blogservice.posts.Post;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Entity
@Table
public class Comment {

    @Id
    @SequenceGenerator(
            name = "comment_sequence",
            sequenceName = "comment_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "comment_sequence"
    )
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", referencedColumnName = "id", nullable = false)
    @JsonIgnore
    private Post post;

//    This is taken care of using PrePersist later
//    This is because the vvaUserId is in the JWT
//    @NotBlank(message = "vvaUserId for comment cannot be null or empty")
    @Size(max = 36, min = 36, message = "vvaUserId for comment must be 36 characters")
    @Column(updatable = false)
    private String vvaUserId;

    @NotBlank(message = "vvaUserName for comment cannot be null or empty")
    @Size(max = 50, message = "vvaUserName for comment cannot be more than 50 characters")
    @Column(length = 500)
    private String vvaUserName;

    @NotBlank(message = "Comment text cannot be null or empty")
    @Size(max = 500, message = "Comment text cannot be more than 500 characters")
    @Column(length = 500)
    private String text;

    // Prevents updates to this column after insert
    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public Comment() {
    }

    public Comment(long id, Post post, String vvaUserId, String vvaUserName, String text, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.post = post;
        this.vvaUserId = vvaUserId;
        this.vvaUserName = vvaUserName;
        this.text = text;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Comment(String vvaUserId, String vvaUserName, String text) {
        this.vvaUserId = vvaUserId;
        this.vvaUserName = vvaUserName;
        this.text = text;
    }

    public Comment(Post post, String vvaUserId, String vvaUserName, String text) {
        this.post = post;
        this.vvaUserId = vvaUserId;
        this.vvaUserName = vvaUserName;
        this.text = text;
    }

    // This makes the createdAt and updatedAt be the time when the row is made
    @PrePersist
    protected void onCreate() {
        if (this.vvaUserId == null) {
            // Ensure vvaUserId is set before persisting
            throw new IllegalStateException("vvaUserId must be set before persisting");
        }
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // This makes the updatedAt be the time when the row is updated
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public long getId() {
        return id;
    }

    public Post getPost() {
        return post;
    }

    public String getVvaUserId() {
        return vvaUserId;
    }

    public String getVvaUserName() {
        return vvaUserName;
    }

    public String getText() {
        return text;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public void setVvaUserId(String vvaUserId) { this.vvaUserId = vvaUserId; }

    public void setVvaUserName(String vvaUserName) {
        this.vvaUserName = vvaUserName;
    }

    public void setText(String text) {
        this.text = text;
    }

}

// PRISMA SCHEMA
//model blog_comment {
//  id          Int      @id @default(autoincrement())
//  blog_id     Int
//  vva_user_id String
//  text        String   @db.VarChar(500)
//  created_at  DateTime @default(now())
//  updatedAt   DateTime @updatedAt
//
//  blog     blog     @relation(fields: [blog_id], references: [id], onDelete: Cascade, onUpdate: Cascade)
//  vva_user vva_user @relation(fields: [vva_user_id], references: [id], onDelete: Cascade, onUpdate: Cascade)
//}