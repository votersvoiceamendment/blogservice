package com.vva.blogservice.post;

import com.vva.blogservice.comment.Comment;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
public class Post {

    @Id
    @SequenceGenerator(
            name = "post_sequence",
            sequenceName = "post_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "post_sequence"
    )
    private long id;

    @Column(updatable = false, nullable = false)
    private String vvaUserId;

    @Column(length = 500, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String text;

    @Column(columnDefinition = "boolean default false")
    private boolean featured = false;  // Default value of 'false' in Java

    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Comment> comments = new ArrayList<>();

    public Post() {
    }

    public Post(
            long id,
            String vvaUserId,
            String title,
            String text,
            boolean featured,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.featured = featured;
        this.text = text;
        this.title = title;
        this.vvaUserId = vvaUserId;
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Post(String vvaUserId, String title, String text, boolean featured) {
        this.vvaUserId = vvaUserId;
        this.title = title;
        this.text = text;
        this.featured = featured;
    }

    public Post(String vvaUserId, String title, String text) {
        this.vvaUserId = vvaUserId;
        this.title = title;
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

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public boolean isFeatured() {
        return featured;
    }

    public String getText() {
        return text;
    }

    public String getTitle() {
        return title;
    }

    public String getVvaUserId() {
        return vvaUserId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setFeatured(boolean featured) {
        this.featured = featured;
    }

    public List<Comment> getComments() {
        return comments;
    }
}

//PRISMA SCHEMA
//model blog {
//  id          Int      @id @default(autoincrement())
//  vva_user_id String
//  title       String   @db.VarChar(500)
//  text        String   @db.Text
//  featured    Boolean  @default(false)
//  createdAt   DateTime @default(now())
//  updatedAt   DateTime @updatedAt
//
//  vva_user     vva_user       @relation(fields: [vva_user_id], references: [id], onDelete: Cascade, onUpdate: Cascade)
//  blog_comment blog_comment[]
//}