package socialmediaspringboot.backend.controller;

import com.cloudinary.Api;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import socialmediaspringboot.backend.dto.ApiResponse;
import socialmediaspringboot.backend.dto.Comment.CommentRequestDTO;
import socialmediaspringboot.backend.dto.Comment.CommentResponseDTO;
import socialmediaspringboot.backend.dto.Comment.CommentUpdateDTO;
import socialmediaspringboot.backend.dto.Post.PostResponseDTO;
import socialmediaspringboot.backend.service.Comment.CommentService;
import socialmediaspringboot.backend.service.Comment.CommentServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {
    @Autowired
    private CommentServiceImpl commentService;

    @PostMapping(value="user/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<CommentResponseDTO> createComment(@PathVariable Long userId,
                                                         @RequestPart("comment") @Valid CommentRequestDTO requestDTO,
                                                         @RequestPart(value = "file", required = false)MultipartFile[] files){
        CommentResponseDTO createdComment = commentService.createComment(userId, requestDTO, files);
        return ApiResponse.<CommentResponseDTO>builder()
                .result(createdComment)
                .message("Create Comment Successfully")
                .build();
    }

    @GetMapping("/{commentId}")
    public ApiResponse<CommentResponseDTO> getCommentById(@PathVariable Long commentId){
        CommentResponseDTO comment = commentService.getCommentById(commentId);
        return ApiResponse.<CommentResponseDTO>builder()
                .result(comment)
                .build();
    }

    @GetMapping("/{postId}")
    public ApiResponse<List<CommentResponseDTO>> getCommentByPost(@PathVariable Long postId){
        List<CommentResponseDTO> commentList = commentService.getAllCommentsByPost(postId);
        return ApiResponse.<List<CommentResponseDTO>>builder()
                .result(commentList)
                .build();
    }

    @GetMapping("/{userId}")
    public ApiResponse<List<CommentResponseDTO>> getCommentByUser(@PathVariable Long userId){
        List<CommentResponseDTO> commentList = commentService.getAllCommentsByUser(userId);
        return ApiResponse.<List<CommentResponseDTO>>builder()
                .result(commentList)
                .build();
    }

    @PutMapping("/{commentId}")
    public ApiResponse<CommentResponseDTO> updateComment(@PathVariable Long commentId, @RequestBody @Valid CommentUpdateDTO updateDTO){
        CommentResponseDTO updatedComment = commentService.updateComment(commentId, updateDTO);
        return ApiResponse.<CommentResponseDTO>builder()
                .result(updatedComment)
                .build();
    }

    @DeleteMapping("/{commentId}")
    public ApiResponse<Void> deleteComment(@PathVariable Long commentId){
        commentService.deleteComment(commentId);
        return ApiResponse.<Void>builder()
                .message("Delete post successfully")
                .build();
    }

}
