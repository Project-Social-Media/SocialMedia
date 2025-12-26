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
        name = "friendship",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"userId", "friendId"})
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FriendShip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long friendshipId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "friendId", nullable = false)
    private User friend;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "friendshipTypeId", nullable = false)
    private FriendShipType friendshipType;

    private LocalDateTime createdAt;


}
