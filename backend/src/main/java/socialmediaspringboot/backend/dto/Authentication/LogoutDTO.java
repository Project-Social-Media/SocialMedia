package socialmediaspringboot.backend.dto.Authentication;

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
