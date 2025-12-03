package socialmediaspringboot.backend.dto.Authentication;

import lombok.*;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationResponse {
    boolean isAuthenticated;
    String token;
}
