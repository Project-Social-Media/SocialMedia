package socialmediaspringboot.backend.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import socialmediaspringboot.backend.model.User.User;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "post")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "postId", nullable = false)
    private Long postId;

    @ManyToOne
    @JoinColumn(name = "authorId", nullable = false)
    @JsonBackReference // child reference
    private User author;

    @Column(columnDefinition = "NVARCHAR(MAX)")
    private String content;

    // =======================
    // Self-reference (Share)
    // =======================
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "originalpostId")
    private Post originalPost;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "privacyId", nullable = false)
    private Privacy privacy;

    @Builder.Default
    private int shareCount =0;
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now(); // set khi tạo
    }
}
