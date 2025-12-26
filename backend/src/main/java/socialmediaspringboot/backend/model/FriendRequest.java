package socialmediaspringboot.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import socialmediaspringboot.backend.model.User.User;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "friendRequest",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"senderId", "receiverId"})
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FriendRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "friendRequestId")
    private Long friendRequestId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "senderId", nullable = false)
    private User sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiverId", nullable = false)
    private User receiver;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "statusId", nullable = false)
    private FriendRequestStatus status;

    @Column(name = "createdAt", nullable = false)
    private LocalDateTime createdAt;
}
