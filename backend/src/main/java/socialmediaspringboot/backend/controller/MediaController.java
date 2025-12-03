package socialmediaspringboot.backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import socialmediaspringboot.backend.dto.ApiResponse;
import socialmediaspringboot.backend.dto.Media.MediaRequestDTO;
import socialmediaspringboot.backend.dto.Media.MediaResponseDTO;
import socialmediaspringboot.backend.mapper.MediaMapper;
import socialmediaspringboot.backend.model.Media;
import socialmediaspringboot.backend.service.Media.MediaServiceImpl;

@RestController
@RequestMapping("api/cloudinary")
@RequiredArgsConstructor
public class MediaController {

    private final MediaServiceImpl mediaService;

    private final MediaMapper mediaMapper;

    @PostMapping("/upload")
    public ApiResponse<MediaResponseDTO> uploadImage(@RequestPart MultipartFile file, @RequestPart MediaRequestDTO request) {
        Media media = mediaService.upload(file, request);
        MediaResponseDTO responseDTO = mediaMapper.toMediaResponseDTO(media);
        return ApiResponse.<MediaResponseDTO>builder()
                .result(responseDTO)
                .build();
    }
}
