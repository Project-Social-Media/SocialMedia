package socialmediaspringboot.backend.dto.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import socialmediaspringboot.backend.model.Gender;
import socialmediaspringboot.backend.model.Media;
import socialmediaspringboot.backend.model.Post;
import socialmediaspringboot.backend.model.Role;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {
    private long userId;
    private String email;
    private String password;
    private String firstname;
    private String lastname;
    private Gender gender;
    private Date birth;
    private Media profilePicture;
    private Media backgroundPicture;
    private LocalDateTime createdAt;
    Set<Role> roles;
    private List<Post> posts = new ArrayList<>();
}
