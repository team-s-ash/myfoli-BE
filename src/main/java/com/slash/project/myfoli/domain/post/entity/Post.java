package com.slash.project.myfoli.domain.post.entity;

import com.slash.project.myfoli.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "post_tbl")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Builder.Default
    private int viewCount = 0;

    @Builder.Default
    private int likeCount = 0;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Setter
    private Status status;

    @ElementCollection
    @CollectionTable(name = "post_images", joinColumns = @JoinColumn(name = "post_id"))
    @Column(name = "image_url")
    @Builder.Default
    private List<String> imageUrls = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Helper method to add an image URL
    public void addImage(String imageUrl) {
        if (this.imageUrls == null) {
            this.imageUrls = new ArrayList<>();
        }
        this.imageUrls.add(imageUrl);
    }

    // Update post information
    public void update(String title, String content, Status status) {
        this.title = title;
        this.content = content;
        this.status = status;
    }

    // Increment view count
    public void incrementViewCount() {
        this.viewCount++;
    }

    // Increment like count
    public void incrementLikeCount() {
        this.likeCount++;
    }

    // Decrement like count
    public void decrementLikeCount() {
        if (this.likeCount > 0) {
            this.likeCount--;
        }
    }
}
