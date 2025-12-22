package socialmediaspringboot.backend.dto.Comment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import socialmediaspringboot.backend.model.Media;
import socialmediaspringboot.backend.model.Privacy;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponseDTO {
    private Long commentId;
    private String content;
    private Long postId;
    private Long userId;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<Media> mediaList = new ArrayList<>();
}
