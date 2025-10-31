package socialmediaspringboot.backend.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import socialmediaspringboot.backend.dto.ApiResponse;
import socialmediaspringboot.backend.dto.UserDTO;
import socialmediaspringboot.backend.model.User.User;
import socialmediaspringboot.backend.service.User.UserServiceImpl;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    @PostMapping("/create")
    public ApiResponse<User> createUser(@RequestBody @Valid UserDTO request){
        ApiResponse<User> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userService.createUser(request));
        return apiResponse;
    }
}
