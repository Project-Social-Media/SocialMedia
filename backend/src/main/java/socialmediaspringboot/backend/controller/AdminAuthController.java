package socialmediaspringboot.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import socialmediaspringboot.backend.dto.ApiResponse;
import socialmediaspringboot.backend.dto.AuthenticationRequest;
import socialmediaspringboot.backend.dto.AuthenticationResponse;
import socialmediaspringboot.backend.service.Authentication.AuthenticationServiceImpl;

@RestController
@RequestMapping("/auth/admin")
public class AdminAuthController {

    @Autowired
    AuthenticationServiceImpl authenticationService;

    @PostMapping("/login")
    private ApiResponse<AuthenticationResponse> userAuthenticate(@RequestBody AuthenticationRequest request) {
        var authResult = authenticationService.loginWithRole(request);
        return ApiResponse.<AuthenticationResponse>builder()
                .code(1)
                .message("")
                .result(authResult)
                .build();
    }
}
