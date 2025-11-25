package socialmediaspringboot.backend.dto.Post;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import socialmediaspringboot.backend.model.Post;
import socialmediaspringboot.backend.model.Privacy;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostDTO {
    private String content;
    private Privacy privacy;
    private Post originalPost;
}
