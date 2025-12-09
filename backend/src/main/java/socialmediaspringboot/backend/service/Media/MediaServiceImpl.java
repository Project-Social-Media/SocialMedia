package socialmediaspringboot.backend.service.Media;

import com.cloudinary.Cloudinary;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import socialmediaspringboot.backend.dto.Media.MediaRequestDTO;
import socialmediaspringboot.backend.dto.Media.MediaResponseDTO;
import socialmediaspringboot.backend.exception.AppException;
import socialmediaspringboot.backend.exception.ErrorCode;
import socialmediaspringboot.backend.mapper.MediaMapper;
import socialmediaspringboot.backend.model.Media;
import socialmediaspringboot.backend.model.MediaType;
import socialmediaspringboot.backend.model.User.User;
import socialmediaspringboot.backend.repository.MediaRepository;
import socialmediaspringboot.backend.repository.MediaTypeRepository;
import socialmediaspringboot.backend.repository.UserRepository;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MediaServiceImpl implements MediaService {
    private final Cloudinary cloudinary;

    private final MediaRepository mediaRepository;

    private final MediaTypeRepository mediaTypeRepository;

    private final UserRepository userRepository;

    private final MediaMapper mediaMapper;

    @Override
    public MediaResponseDTO upload(MultipartFile file, MediaRequestDTO request){
        Media media = new Media();
        try{
            //logic to check file type/extension and assign mediaType
            String fileName = file.getOriginalFilename();
            String extension = "";
            if(fileName != null && fileName.contains(".")){
                extension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
            }
            List<String> allowedImgType = List.of("jpg","jpeg","png");
            List<String> allowedVidType = List.of("mp4","mp3","mov");
            if(!allowedImgType.contains(extension) && !allowedVidType.contains(extension)){
                throw new RuntimeException("Unsupported file type: " + extension); // this needs to be assigned a unique code in global exception handler
            }

            //upload file to cloudinary
            Map data = this.cloudinary.uploader().upload(file.getBytes(), Map.of());

            //set entity properties
            media.setMediaUrl((String) data.get("secure_url"));
            if(allowedImgType.contains(extension)){
                MediaType imgType = mediaTypeRepository.findById(1)
                        .orElseThrow(()-> new RuntimeException("Media Type not found"));
                media.setMediatypeId(imgType);
            }else if(allowedVidType.contains(extension)){
                MediaType vidType = mediaTypeRepository.findById(2)
                        .orElseThrow(()-> new RuntimeException("Media Type not found"));
                media.setMediatypeId(vidType);
            }
            media.setUploadorder(request.getUploadorder());
            //logic to check logged-in user - this user is the author of the media
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
            Long userId = user.getUserId();
            media.setUserId(user);
            media.setCloudId((String) data.get("public_id"));
            media.setCreatedAt(LocalDateTime.now());
            Media saved = mediaRepository.save(media);
            return mediaMapper.toMediaResponseDTO(saved);
        }catch(IOException io){
            throw new RuntimeException("Media upload fail.");
        }
    }
}
