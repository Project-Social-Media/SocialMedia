package socialmediaspringboot.backend.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import socialmediaspringboot.backend.dto.ApiResponse;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @GetMapping("/test")
    public ApiResponse<String> testAdminAccess(){
        return ApiResponse.<String>builder()
                .code(1)
                .message("Admin access successfully")
                .result("Hello Admin")
                .build();
    }
}
