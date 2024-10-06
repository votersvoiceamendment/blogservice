package com.vva.blogservice.posts;

import com.vva.blogservice.comments.Comment;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

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

//    This is taken care of using PrePersist later
//    This is because the vvaUserId is in the JWT
//    @NotBlank(message = "vvaUserId for post cannot be null or empty")
    @Size(max = 36, min = 36, message = "vvaUserId for post must be 36 characters")
    @Column(updatable = false, nullable = false)
    private String vvaUserId;

    @NotBlank(message = "Post title cannot be null or empty")
    @Size(max = 500, message = "Post title cannot be more than 500 characters")
    @Column(length = 500, nullable = false)
    private String title;

    @NotBlank(message = "Post text cannot be null or empty")
    @Size(max = 20000, message = "Post text cannot be more than 20000 characters")
    @Column(columnDefinition = "TEXT", nullable = false)
    private String text;

    @Column(columnDefinition = "boolean default false")
    private boolean featured = false;

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

    public void setVvaUserId(String vvaUserId) { this.vvaUserId = vvaUserId; }

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

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", vvaUserId='" + vvaUserId + '\'' +
                ", title='" + title + '\'' +
                ", text='" + text + '\'' +
                ", featured=" + featured +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", comments=" + comments +
                '}';
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