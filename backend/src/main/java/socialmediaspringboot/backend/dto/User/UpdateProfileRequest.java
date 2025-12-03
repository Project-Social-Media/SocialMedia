package socialmediaspringboot.backend.dto.User;


import lombok.Data;

import java.util.Date;

@Data
public class UpdateProfileRequest {
    private String firstname;
    private String lastname;
    private String password;
    private Integer genderId;
    private Date birth;
}
