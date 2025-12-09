package socialmediaspringboot.backend.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import socialmediaspringboot.backend.dto.ApiResponse;
import socialmediaspringboot.backend.dto.User.ChangePicturePostRequest;
import socialmediaspringboot.backend.dto.User.UpdateProfileRequest;
import socialmediaspringboot.backend.dto.User.UserDTO;
import socialmediaspringboot.backend.dto.User.UserResponseDTO;
import socialmediaspringboot.backend.model.User.User;
import socialmediaspringboot.backend.repository.UserRepository;
import socialmediaspringboot.backend.service.User.UserServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/create")
    public ApiResponse<User> createUser(@RequestBody @Valid UserDTO request){
        ApiResponse<User> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userService.createUser(request));
        return apiResponse;
    }

    @GetMapping("/list")
    ApiResponse<List<User>> getListUser(){
        return ApiResponse.<List<User>>builder()
                .result(userService.getAllUsers())
                .build();
    }

    @GetMapping("/{userId}")
    ApiResponse<UserResponseDTO> getUser(@PathVariable("userId") Long userId){
        return ApiResponse.<UserResponseDTO>builder()
                .result(userService.getUser(userId))
                .build();
    }


    @GetMapping("/me")
    public ApiResponse<UserResponseDTO> getCurrentUser(){
       return ApiResponse.<UserResponseDTO>builder()
               .result(userService.getMyInfo())
               .build();
    }

    @PutMapping("/update")
    public ApiResponse<UserResponseDTO> updateProfile(@RequestBody @Valid UpdateProfileRequest request){
        return ApiResponse.<UserResponseDTO>builder()
                .result(userService.updateUser(request))
                .build();
    }
    @PutMapping("/profile-picture")
    public ApiResponse<UserResponseDTO> changeProfilePicture(
            @RequestPart("file") MultipartFile file,
            @RequestPart("data") ChangePicturePostRequest request
    ) {
        return ApiResponse.<UserResponseDTO>builder()
                .result(userService.changeProfilePicture(file, request))
                .build();
    }
    @PutMapping("/background-picture")
    public ApiResponse<UserResponseDTO> changeBackgroundPicture(
            @RequestPart("file") MultipartFile file,
            @RequestPart("data") ChangePicturePostRequest request
    ) {
        return ApiResponse.<UserResponseDTO>builder()
                .result(userService.changeBackgroundPicture(file, request))
                .build();
    }


    @DeleteMapping("/{userId}")
    public ApiResponse<Void> deleteUser(@PathVariable("userId") Long userId){
        userService.deleteUser(userId);
        return ApiResponse.<Void>builder()
                .message("User deleted successfully")
                .build();
    }
}
