package socialmediaspringboot.backend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import socialmediaspringboot.backend.dto.Comment.CommentRequestDTO;
import socialmediaspringboot.backend.dto.Comment.CommentResponseDTO;
import socialmediaspringboot.backend.dto.Comment.CommentUpdateDTO;
import socialmediaspringboot.backend.dto.Media.MediaRequestDTO;
import socialmediaspringboot.backend.dto.Post.PostDTO;
import socialmediaspringboot.backend.model.Comment;
import socialmediaspringboot.backend.model.Post;
import socialmediaspringboot.backend.repository.MediaRepository;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    @Mapping(target = "commentId", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "post", ignore = true)
    Comment toComment(CommentRequestDTO request);

    @Mapping(target = "commentId", ignore = false)
    @Mapping(source = "author.userId", target = "userId")
    @Mapping(source = "post.postId", target = "postId")
    CommentResponseDTO toCommentResponseDTO (Comment media);

    // UPDATE
    @Mapping(target = "post", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateComment(CommentUpdateDTO updateDTO, @MappingTarget Comment comment);
}
