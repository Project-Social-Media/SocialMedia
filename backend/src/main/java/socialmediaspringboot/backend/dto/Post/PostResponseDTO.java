package socialmediaspringboot.backend.dto.Post;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import socialmediaspringboot.backend.model.Media;
import socialmediaspringboot.backend.model.Post;
import socialmediaspringboot.backend.model.Privacy;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    private Privacy privacy;

    private int shareCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<Media> mediaList = new ArrayList<>();

}
