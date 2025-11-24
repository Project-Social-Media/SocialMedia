package socialmediaspringboot.backend.dto.Post;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostResponseDTO {
    private Long postId;
    private String content;
    private Long authorId;
    private String authorEmail;

    private Long originalPostId;
    private Long privacyId;

    private int shareCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
