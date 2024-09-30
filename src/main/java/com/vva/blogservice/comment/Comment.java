package com.vva.blogservice.comment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vva.blogservice.post.Post;
import jakarta.persistence.*;

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

    // Prevents updates after the entity is created
    @Column(updatable = false)
    private String vvaUserId;

    @Column(length = 500)
    private String vvaUserName;

    @Column(length = 500)
    private String text;

    // Prevents updates to this column after insert
    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public Comment() {
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