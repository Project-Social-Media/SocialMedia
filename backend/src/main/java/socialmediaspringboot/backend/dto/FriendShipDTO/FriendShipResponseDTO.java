package socialmediaspringboot.backend.dto.FriendShipDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FriendShipResponseDTO {
    private Long friendshipId;
    private Long userId;
    private Long friendId;
    private String friendName;
    private String friendshipType;
    private LocalDateTime createdAt;
}
