package socialmediaspringboot.backend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import socialmediaspringboot.backend.dto.Media.MediaRequestDTO;
import socialmediaspringboot.backend.dto.Media.MediaResponseDTO;
import socialmediaspringboot.backend.model.Media;


@Mapper(componentModel = "spring")
public interface MediaMapper {
    @Mapping(target = "mediaId", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "post", ignore = true)
    Media toMedia(MediaRequestDTO request);

    @Mapping(target = "mediaId", ignore = false)
    @Mapping(source = "mediatypeId.mediaTypeId", target = "mediatypeId")
    @Mapping(source = "userId.userId", target = "userId")
    @Mapping(source = "post.postId", target = "postId")
    MediaResponseDTO toMediaResponseDTO (Media media);
}
