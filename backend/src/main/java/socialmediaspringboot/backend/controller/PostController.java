package socialmediaspringboot.backend.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import socialmediaspringboot.backend.dto.ApiResponse;
import socialmediaspringboot.backend.dto.Media.MediaRequestDTO;
import socialmediaspringboot.backend.dto.Post.PostDTO;
import socialmediaspringboot.backend.dto.Post.PostResponseDTO;
import socialmediaspringboot.backend.model.Post;
import socialmediaspringboot.backend.service.Post.PostServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    PostServiceImpl postService;

    @PostMapping("/user/{userId}")
    public ApiResponse<PostResponseDTO> createPost(@PathVariable Long userId,
                                                   @RequestPart("post") @Valid PostDTO postDTO,
                                                   @RequestPart(value = "file", required = false) MultipartFile[] files,
                                                   @RequestPart(value = "media", required = false)MediaRequestDTO mediaRequestDTO
                                                   ){
        PostResponseDTO createdPost = postService.createPost(userId,postDTO, files, mediaRequestDTO);
        return ApiResponse.<PostResponseDTO>builder()
                .result(createdPost)
                .message("Create Post successfully")
                .build();
    }

    @PutMapping("/{postId}")
    public ApiResponse<PostResponseDTO> updatePost(@PathVariable Long postId, @RequestBody @Valid PostDTO postDTO){
        PostResponseDTO updatedPost = postService.updatePost(postId,postDTO);
        return ApiResponse.<PostResponseDTO>builder()
                .result(updatedPost)
                .message("Update Post successfully")
                .build();

    }

    @DeleteMapping("/{postId}")
    public ApiResponse<Void> deletePost(@PathVariable Long postId){
        postService.deletePost(postId);
        return ApiResponse.<Void>builder()
                .message("Delete post successfully")
                .build();
    }

    @GetMapping("/list/{userId}")
    public ApiResponse<List<Post>> getAllPosts(@PathVariable Long userId){
        List<Post> posts = postService.getAllPost(userId);
        return ApiResponse.<List<Post>>builder()
                .result(posts)
                .build();
    }

    @GetMapping("/{postId}")
    public ApiResponse<PostResponseDTO> getPostById(@PathVariable Long postId){
        PostResponseDTO post = postService.getPostById(postId);
        return ApiResponse.<PostResponseDTO>builder()
                .result(post)
                .build();

    }

    @GetMapping("/search")
    public ApiResponse<List<PostResponseDTO>> getPostByKeyword(@RequestParam String keyword) {
        List<PostResponseDTO> posts = postService.getPostByKeyword(keyword);
        return ApiResponse.<List<PostResponseDTO>>builder()
                .result(posts)
                .build();
    }

    @PostMapping("/{originalPostId}/share/user/{userId}")
    public ApiResponse<PostResponseDTO> sharePost(
            @PathVariable Long originalPostId,
            @PathVariable Long userId,
            @RequestParam(required = false) String caption) {
        PostResponseDTO sharedPost = postService.sharePost(originalPostId, userId, caption);
        return ApiResponse.<PostResponseDTO>builder()
                .result(sharedPost)
                .message("Post shared successfully")
                .build();
    }
}
