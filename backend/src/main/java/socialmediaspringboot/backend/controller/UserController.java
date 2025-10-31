package socialmediaspringboot.backend.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import socialmediaspringboot.backend.dto.ApiResponse;
import socialmediaspringboot.backend.dto.UpdateProfileRequest;
import socialmediaspringboot.backend.dto.UserDTO;
import socialmediaspringboot.backend.model.Gender;
import socialmediaspringboot.backend.model.User.User;
import socialmediaspringboot.backend.repository.GenderRepository;
import socialmediaspringboot.backend.repository.UserRepository;
import socialmediaspringboot.backend.service.User.UserServiceImpl;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private GenderRepository genderRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/create")
    public ApiResponse<User> createUser(@RequestBody @Valid UserDTO request){
        ApiResponse<User> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userService.createUser(request));
        return apiResponse;
    }


    @GetMapping("/me")
    public ApiResponse<User> getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new RuntimeException("User not found"));
        ApiResponse<User> response = new ApiResponse<>();
        response.setResult(user);
        response.setMessage("Fetch successfully");
        return response;
    }

    @PutMapping("/me")
    public ApiResponse<User> updateProfile(@RequestBody @Valid UpdateProfileRequest request){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setFirstname(request.getFirstname());
        user.setLastname(request.getLastname());
        user.setBirth(request.getBirth());

        if (request.getGenderId() != null){
            Gender gender = genderRepository.findById(request.getGenderId())
                    .orElseThrow(() -> new RuntimeException("Gender not found"));
            user.setGender(gender);
        }

        User updatedUser = userRepository.save(user);
        ApiResponse<User> response = new ApiResponse<>();
        response.setResult(updatedUser);
        response.setMessage("Update successfully");
        return response;
    }

}
