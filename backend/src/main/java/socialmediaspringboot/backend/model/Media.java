package socialmediaspringboot.backend.model;

import jakarta.persistence.*;
import lombok.*;
import socialmediaspringboot.backend.model.User.User;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "media")
public class Media {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mediaId", nullable = false)
    private Long mediaId;

    @Column(name = "mediaUrl") // <-- Thêm @Column cho rõ ràng
    private String mediaUrl;

    @ManyToOne
    @JoinColumn(name = "mediatypeId")
    private MediaType mediatypeId;

    @Column(name = "uploadorder")
    private int uploadorder;

    @Column(name = "cloudId")
    private String cloudId;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User userId;

    @ManyToOne
    @JoinColumn(name = "postId")
    private Post postId;

//    @ManyToOne
//    @JoinColumn(name = "commentId")
//    private Comment commentId;

    @Column(name = "createdAt")
    private LocalDateTime createdAt;
}
