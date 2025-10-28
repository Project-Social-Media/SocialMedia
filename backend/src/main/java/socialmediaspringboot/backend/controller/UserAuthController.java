package socialmediaspringboot.backend.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/user")
public class UserAuthController {

    @PostMapping("/login")
    private void userAuthenticate(){}
}
