package socialmediaspringboot.backend.service.Authentication;

import socialmediaspringboot.backend.dto.Authentication.AuthenticationRequest;
import socialmediaspringboot.backend.dto.Authentication.AuthenticationResponse;
import socialmediaspringboot.backend.dto.Authentication.LogoutDTO;

public interface AuthenticationService {
    AuthenticationResponse login(AuthenticationRequest request);
    AuthenticationResponse loginWithRole(AuthenticationRequest request);
    void logout(LogoutDTO request);
}
