package socialmediaspringboot.backend.service.Authentication;


import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContextException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import socialmediaspringboot.backend.dto.AuthenticationRequest;
import socialmediaspringboot.backend.dto.AuthenticationResponse;
import socialmediaspringboot.backend.dto.LogoutDTO;
import socialmediaspringboot.backend.exception.AppException;
import socialmediaspringboot.backend.exception.ErrorCode;
import socialmediaspringboot.backend.model.User.MyUserDetails;
import socialmediaspringboot.backend.repository.UserRepository;
import socialmediaspringboot.backend.security.JwtService;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public AuthenticationResponse login(AuthenticationRequest request) {
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ApplicationContextException("Error"));//placeholder for exception handling after implement global handler
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());

        if(!authenticated) throw new RuntimeException("error");//placeholder for global handler
//        var token = generateToken();

        return AuthenticationResponse.builder()
                .token("placeholderString")
                .isAuthenticated(true)
                .build();
    }

    @Override
    public AuthenticationResponse loginWithRole(AuthenticationRequest request) {
        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var userDetails = (MyUserDetails) authentication.getPrincipal();

        var token = jwtService.generateToken(userDetails);

        return AuthenticationResponse.builder()
                .token(token)
                .isAuthenticated(true)
                .build();
    }

    @Override
    public void logout(LogoutDTO request) {

    }
}
