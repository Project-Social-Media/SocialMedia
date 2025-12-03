package socialmediaspringboot.backend.service.Media;

import com.cloudinary.Cloudinary;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import socialmediaspringboot.backend.dto.Media.MediaRequestDTO;
import socialmediaspringboot.backend.model.Media;
import socialmediaspringboot.backend.repository.MediaRepository;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MediaServiceImpl implements MediaService {
    private final Cloudinary cloudinary;

    private final MediaRepository mediaRepository;

    @Override
    public Media upload(MultipartFile file, MediaRequestDTO request){
        Media media = new Media();
        try{
            Map data = this.cloudinary.uploader().upload(file.getBytes(), Map.of());
            media.setMediaUrl((String) data.get("secure_url"));
            media.setMediatypeId(request.getMediatypeId());
            media.setUploadorder(request.getUploadorder());
            media.setUserId(request.getUserId());
            media.setPostId(request.getPostId());
            media.setCreatedAt(LocalDateTime.now());
            return mediaRepository.save(media);
        }catch(IOException io){
            throw new RuntimeException("Media upload fail.");
        }
    }
}
