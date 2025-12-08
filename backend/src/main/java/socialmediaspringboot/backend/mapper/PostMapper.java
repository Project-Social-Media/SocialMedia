package socialmediaspringboot.backend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import socialmediaspringboot.backend.dto.Post.PostDTO;
import socialmediaspringboot.backend.dto.Post.PostResponseDTO;
import socialmediaspringboot.backend.model.Post;

@Mapper(componentModel = "spring")
public interface PostMapper {
    @Mapping(target = "postId", ignore = true)
    @Mapping(target = "author", ignore = true)
    //@Mapping(target = "privacy", ignore = true)
    @Mapping(target = "originalPost", ignore = true)
    @Mapping(target = "shareCount", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "mediaList", ignore = true)
    Post toPost(PostDTO dto);

    // Entity -> ResponseDTO
    @Mapping(target = "authorId", source = "author.userId")
    @Mapping(target = "authorEmail", source = "author.email")
    @Mapping(target = "originalPostId", source = "originalPost.postId")
    @Mapping(target = "privacy", source = "privacy")
    PostResponseDTO toPostResponseDTO(Post post);

    // UPDATE
    @Mapping(target = "postId", ignore = true)
    @Mapping(target = "author", ignore = true)
    //@Mapping(target = "privacy", ignore = true)
    @Mapping(target = "originalPost", ignore = true)
    @Mapping(target = "shareCount", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "mediaList", ignore = true)
    void updatePostFromDTO(PostDTO dto, @MappingTarget Post post);
}
