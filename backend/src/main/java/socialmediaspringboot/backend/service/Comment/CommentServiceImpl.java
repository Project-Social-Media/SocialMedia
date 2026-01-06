package socialmediaspringboot.backend.service.Comment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.stylesheets.MediaList;
import socialmediaspringboot.backend.dto.Comment.CommentRequestDTO;
import socialmediaspringboot.backend.dto.Comment.CommentResponseDTO;
import socialmediaspringboot.backend.dto.Comment.CommentUpdateDTO;
import socialmediaspringboot.backend.dto.Media.MediaRequestDTO;
import socialmediaspringboot.backend.dto.Media.MediaResponseDTO;
import socialmediaspringboot.backend.exception.AppException;
import socialmediaspringboot.backend.exception.ErrorCode;
import socialmediaspringboot.backend.mapper.CommentMapper;
import socialmediaspringboot.backend.mapper.MediaMapper;
import socialmediaspringboot.backend.model.Comment;
import socialmediaspringboot.backend.model.Media;
import socialmediaspringboot.backend.model.MediaType;
import socialmediaspringboot.backend.model.Post;
import socialmediaspringboot.backend.model.User.User;
import socialmediaspringboot.backend.repository.*;
import socialmediaspringboot.backend.service.Media.MediaService;
import socialmediaspringboot.backend.service.Media.MediaServiceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService{

    private final CommentRepository commentRepository;

    private final CommentMapper commentMapper;

    private final MediaServiceImpl mediaService;

    private final MediaMapper mediaMapper;

    private final MediaRepository mediaRepository;

    private final MediaTypeRepository mediaTypeRepository;

    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Transactional
    @Override
    public CommentResponseDTO createComment(Long userId, Long postId, CommentRequestDTO requestDTO, MultipartFile[] files) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_FOUND)
        );

        Post post = postRepository.findById(postId).orElseThrow(
                () -> new AppException(ErrorCode.POST_NOT_FOUND)
        );

        Comment cmt = commentMapper.toComment(requestDTO);
        cmt.setAuthor(user);
        cmt.setPost(post);
        cmt.setContent(requestDTO.getContent());
        cmt.setCreatedAt(LocalDateTime.now());
        commentRepository.save(cmt);
        if(files != null){
            int order = 0;
            for (MultipartFile file : files) {
                MediaRequestDTO req = new MediaRequestDTO();
                req.setUploadorder(order++);

                MediaResponseDTO uploaded = mediaService.upload(file, req);
                Media media = mediaMapper.toMedia(req);

                media.setMediaUrl(uploaded.getMediaUrl());
                media.setMediaSize(uploaded.getMediaSize());
                media.setCloudId(uploaded.getCloudId());

                media.setUserId(user);
                media.setCommentId(cmt);

                if (uploaded.getMediatypeId() != null) {
                    MediaType type = mediaTypeRepository.findById(uploaded.getMediatypeId())
                            .orElseThrow(() -> new RuntimeException("Media type not found"));
                    media.setMediatypeId(type);
                }
                media.setCreatedAt(uploaded.getCreatedAt());
                mediaRepository.save(media);
            }
        }
        return commentMapper.toCommentResponseDTO(cmt);
    }

    @Override
    public CommentResponseDTO getCommentById(Long cmtId) {
        Comment cmt = commentRepository.findById(cmtId).orElseThrow(
                () -> new AppException(ErrorCode.COMMENT_NOT_FOUND)
        );
        return commentMapper.toCommentResponseDTO(cmt);
    }

    @Override
    public List<CommentResponseDTO> getAllCommentsByPost(Long postId) {
        return commentRepository.findALlByPost_PostId(postId)
                .stream().map(commentMapper::toCommentResponseDTO)
                .toList();
    }

    @Override
    public List<CommentResponseDTO> getAllCommentsByUser(Long userId) {
        return commentRepository.findALlCommentByAuthor_UserId(userId)
                .stream().map(commentMapper::toCommentResponseDTO)
                .toList();
    }

    @Override
    @Transactional
    public CommentResponseDTO updateComment(Long commentId, CommentUpdateDTO updateDTO) {
        Comment existingCmt = commentRepository.findById(commentId).orElseThrow(
                () -> new AppException(ErrorCode.COMMENT_NOT_FOUND)
        );
        List<Long> deleteMediaIds = updateDTO.getDeleteMediaIds();
        if( deleteMediaIds != null && !deleteMediaIds.isEmpty()  ){
            List<Media> mediaList = mediaRepository.findAllById(deleteMediaIds);
            for(Media media : mediaList){
                if(!media.getCommentId().getCommentId().equals(commentId)){
                    throw new RuntimeException("wrong comment");
                }
            }
            mediaService.deleteMedia(deleteMediaIds);
        }
        commentMapper.updateComment(updateDTO, existingCmt);
        existingCmt.setUpdatedAt(LocalDateTime.now());
        Comment updatedCmt = commentRepository.save(existingCmt);
        return commentMapper.toCommentResponseDTO(updatedCmt);
    }

    @Override
    @Transactional
    public void deleteComment(Long commentId) {
        Comment cmt = commentRepository.findById(commentId).orElseThrow(
                () -> new AppException(ErrorCode.COMMENT_NOT_FOUND)
        );
        List<Media> mediaList = cmt.getMediaList();
        List<Long> mediaIds = mediaList.stream()
                .map(Media::getMediaId)
                .toList();
        mediaService.deleteMedia(mediaIds);
        commentRepository.deleteById(commentId);
    }
}
