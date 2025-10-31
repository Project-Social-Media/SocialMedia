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
@RequestMapping("/auth/user")
public class UserAuthController {

    @Autowired
    private AuthenticationServiceImpl authenticationService;

    @PostMapping("/login")
    private ApiResponse<AuthenticationResponse> userAuthenticate(@RequestBody AuthenticationRequest request){
        var authResult = authenticationService.loginWithRole(request, "USER");
        return ApiResponse.<AuthenticationResponse>builder()
                .result(authResult)
                .build();
    }
}
