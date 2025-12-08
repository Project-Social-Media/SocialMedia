package socialmediaspringboot.backend.dto.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import socialmediaspringboot.backend.model.Privacy;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChangePicturePostRequest {
    private String content;
    private Long privacyId;
}
