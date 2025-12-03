package socialmediaspringboot.backend.dto.Media;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import socialmediaspringboot.backend.model.MediaType;
import socialmediaspringboot.backend.model.Post;
import socialmediaspringboot.backend.model.User.User;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MediaResponseDTO {
    private Long mediaId;
    private String mediaUrl;
    private MediaType mediatypeId;
    private int uploadorder;
    private String cloudId;
    private User userId;
    private Post postId;
    //    private Comment commentId;
    private LocalDateTime createdAt;

}
