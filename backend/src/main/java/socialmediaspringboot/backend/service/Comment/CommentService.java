package socialmediaspringboot.backend.service.Comment;

import org.springframework.web.multipart.MultipartFile;
import socialmediaspringboot.backend.dto.Comment.CommentRequestDTO;
import socialmediaspringboot.backend.dto.Comment.CommentResponseDTO;
import socialmediaspringboot.backend.dto.Comment.CommentUpdateDTO;

import java.util.List;

public interface CommentService {
    public CommentResponseDTO createComment(Long userId, CommentRequestDTO requestDTO, MultipartFile[] files);
    public CommentResponseDTO getCommentById(Long cmtId);
    public List<CommentResponseDTO> getAllCommentsByPost(Long postId);
    public List<CommentResponseDTO> getAllCommentsByUser(Long userId);
    public CommentResponseDTO updateComment(Long commentId, CommentUpdateDTO updateDTO);
    public void deleteComment(Long commentId);

}
