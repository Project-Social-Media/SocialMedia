package socialmediaspringboot.backend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import socialmediaspringboot.backend.dto.ApiResponse;
import socialmediaspringboot.backend.dto.Media.MediaRequestDTO;
import socialmediaspringboot.backend.dto.Media.MediaResponseDTO;
import socialmediaspringboot.backend.dto.Post.PostResponseDTO;
import socialmediaspringboot.backend.mapper.MediaMapper;
import socialmediaspringboot.backend.model.Media;
import socialmediaspringboot.backend.service.Media.MediaServiceImpl;

@RestController
@RequestMapping("api/cloudinary")
@RequiredArgsConstructor
public class MediaController {

    private final MediaServiceImpl mediaService;

    private final MediaMapper mediaMapper;

    //for testing purpose
    @PostMapping("/upload")
    public ApiResponse<MediaResponseDTO> uploadImage(@RequestPart MultipartFile file, @RequestPart @Valid MediaRequestDTO request) {
        if (file == null || file.isEmpty()) {
            throw new RuntimeException("File must not be empty");
        }
        MediaResponseDTO media = mediaService.upload(file, request);
        return ApiResponse.<MediaResponseDTO>builder()
                .result(media)
                .build();
    }

    @GetMapping("/{mediaId}")
    public ApiResponse<MediaResponseDTO> getPostById(@PathVariable Long mediaId) {
        MediaResponseDTO media = mediaService.getMediaById(mediaId);
        return ApiResponse.<MediaResponseDTO>builder()
                .result(media)
                .build();
    }
}
