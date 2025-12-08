package socialmediaspringboot.backend.service.Media;

import org.springframework.web.multipart.MultipartFile;
import socialmediaspringboot.backend.dto.Media.MediaRequestDTO;
import socialmediaspringboot.backend.dto.Media.MediaResponseDTO;
import socialmediaspringboot.backend.model.Media;

public interface MediaService {
    public MediaResponseDTO upload(MultipartFile file, MediaRequestDTO request);
}
