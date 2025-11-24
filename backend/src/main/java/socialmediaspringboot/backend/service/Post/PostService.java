package socialmediaspringboot.backend.service.Post;

import socialmediaspringboot.backend.dto.Post.PostDTO;
import socialmediaspringboot.backend.dto.Post.PostResponseDTO;


import java.util.List;

public interface PostService {

    PostResponseDTO createPost(Long userId, PostDTO postDTO);

    PostResponseDTO updatePost(Long postId, PostDTO postDTO);

    void deletePost(Long postId);

    List<PostResponseDTO> getAllPost();
    PostResponseDTO getPostById(Long postId);
    List<PostResponseDTO> getPostByKeyword(String keyword);

    PostResponseDTO sharePost(Long originalPostId, Long userId, String caption);

}
