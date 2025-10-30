package socialmediaspringboot.backend.dto;


import lombok.Data;

import java.util.Date;

@Data
public class UpdateProfileRequest {
    private String firstname;
    private String lastname;
    private Integer genderId;
    private Date birth;
}
