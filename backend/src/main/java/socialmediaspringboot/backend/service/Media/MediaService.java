package socialmediaspringboot.backend.service.Media;

import org.springframework.web.multipart.MultipartFile;
import socialmediaspringboot.backend.dto.Media.MediaRequestDTO;
import socialmediaspringboot.backend.dto.Media.MediaResponseDTO;
import socialmediaspringboot.backend.model.Media;

import java.util.List;

public interface MediaService {
    public MediaResponseDTO upload(MultipartFile file, MediaRequestDTO request);
    public MediaResponseDTO getMediaById(Long mediaId);
    public void deleteMedia(List<Long> mediaId);
}
