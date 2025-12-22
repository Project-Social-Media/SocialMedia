package socialmediaspringboot.backend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import socialmediaspringboot.backend.model.User.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "comment")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "commentId", nullable = false)
    private Long commentId;

    @ManyToOne
    @JoinColumn(name = "authorId", nullable = false)
    @JsonBackReference("user-comment") // child reference
    private User author;

    @ManyToOne
    @JoinColumn(name = "postId", nullable = false)
    @JsonBackReference("post-comment") // child reference
    private Post post;

    @Column(columnDefinition = "NVARCHAR(MAX)")
    private String content;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @JsonManagedReference("comment-media") // parent reference
    @OneToMany(mappedBy = "commentId", cascade = CascadeType.ALL, orphanRemoval = false)
    @Column(nullable = true)
    private List<Media> mediaList = new ArrayList<>();
}
