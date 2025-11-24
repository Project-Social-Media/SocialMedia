package socialmediaspringboot.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.joda.time.DateTime;
import org.springframework.cglib.core.Local;
import socialmediaspringboot.backend.model.Gender;
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
    private LocalDateTime createdAt;
    Set<Role> roles;
    private List<Post> posts = new ArrayList<>();
}
