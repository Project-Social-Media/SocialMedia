package socialmediaspringboot.backend.dto.Post;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import socialmediaspringboot.backend.model.Post;
import socialmediaspringboot.backend.model.Privacy;

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

    private Post originalPost;
    private Privacy privacy;

    private int shareCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
