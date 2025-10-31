package socialmediaspringboot.backend.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class LogoutDTO {
    String token;
}
