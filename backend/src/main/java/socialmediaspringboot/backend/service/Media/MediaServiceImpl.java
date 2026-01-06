package socialmediaspringboot.backend.service.Media;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import io.netty.util.internal.ObjectUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import socialmediaspringboot.backend.dto.Media.MediaRequestDTO;
import socialmediaspringboot.backend.dto.Media.MediaResponseDTO;
import socialmediaspringboot.backend.dto.Post.PostResponseDTO;
import socialmediaspringboot.backend.exception.AppException;
import socialmediaspringboot.backend.exception.ErrorCode;
import socialmediaspringboot.backend.mapper.MediaMapper;
import socialmediaspringboot.backend.model.Media;
import socialmediaspringboot.backend.model.MediaType;
import socialmediaspringboot.backend.model.Post;
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
            //logic to check file size and throw an error if file is bigger than 25MB
            long fileSize = file.getSize();
            long maxSize = 25 * 1024 * 1024;
            if(fileSize > maxSize){
                throw new RuntimeException("Can only upload files not bigger than 25MB"); // this needs to be assigned a unique code in global exception handler
            }
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

            Number sizeInBytes = (Number) data.get("bytes");
            media.setMediaSize(sizeInBytes.longValue());
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
//            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//            String email = authentication.getName();
//            User user = userRepository.findByEmail(email)
//                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
//            Long userId = user.getUserId();
//            media.setUserId(user);
            media.setCloudId((String) data.get("public_id"));
            media.setCreatedAt(LocalDateTime.now());
            return mediaMapper.toMediaResponseDTO(media);
        }catch(IOException io){
            throw new RuntimeException("Media upload fail.");
        }
    }

    @Override
    public MediaResponseDTO getMediaById(Long mediaId) {
        Media media =  mediaRepository.findById(mediaId)
                .orElseThrow(() -> new RuntimeException("Media not found"));
        return mediaMapper.toMediaResponseDTO(media);
    }

    private String resolveResourceType(MediaType mediaType) {
        return switch (mediaType.getMediaTypeId().intValue()) {
            case 1 -> "image";
            case 2 -> "video";
            default -> throw new AppException(ErrorCode.MEDIA_TYPE_NOT_FOUND);
        };
    }

    @Override
    public void deleteMedia(List<Long> mediaIds){
        List<Media>  mediaList = mediaRepository.findAllById(mediaIds);
        if(mediaList.size() != mediaIds.size()){
            throw new AppException(ErrorCode.MEDIA_NOT_FOUND);
        }
        for(Media media: mediaList){
            String resourceType = resolveResourceType(media.getMediatypeId());
            Map result;
            try{
                result = this.cloudinary.uploader().destroy(
                        media.getCloudId(),
                        ObjectUtils.asMap("resource_type", resourceType)
                );

            }catch(IOException io){
                throw new RuntimeException("Media delete fail.");
            }
            if(!"ok".equals(result.get("result"))){
                throw new RuntimeException("Delete media failed.");
            }
        }
        mediaRepository.deleteAll(mediaList);
    }
}
