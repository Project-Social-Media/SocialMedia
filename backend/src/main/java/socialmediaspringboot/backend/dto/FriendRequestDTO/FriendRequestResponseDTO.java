package socialmediaspringboot.backend.dto.FriendRequestDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FriendRequestResponseDTO {
    private Long friendRequestId;
    private Long senderId;
    private String senderName;
    private String senderAvatar;

    private Long receiverId;

    private String status;

    private LocalDateTime createdAt;
}
