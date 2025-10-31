package socialmediaspringboot.backend.service.Authentication;

import socialmediaspringboot.backend.dto.AuthenticationRequest;
import socialmediaspringboot.backend.dto.AuthenticationResponse;
import socialmediaspringboot.backend.dto.LogoutDTO;

public interface AuthenticationService {
    AuthenticationResponse login(AuthenticationRequest request);
    AuthenticationResponse loginWithRole(AuthenticationRequest request, String requiredRoleName);
    void logout(LogoutDTO request);
}
