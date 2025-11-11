package socialmediaspringboot.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.joda.time.DateTime;
import socialmediaspringboot.backend.model.Gender;
import socialmediaspringboot.backend.model.Role;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private String email;
    private String password;
    private String firstname;
    private String lastname;
    private Gender gender;
    private Date birth;
//    private DateTime createdAt;
    Set<Role> roles;
}
