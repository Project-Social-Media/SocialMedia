package socialmediaspringboot.backend.service.Post;

import jakarta.transaction.TransactionScoped;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import socialmediaspringboot.backend.dto.Post.PostDTO;
import socialmediaspringboot.backend.dto.Post.PostResponseDTO;
import socialmediaspringboot.backend.mapper.PostMapper;
import socialmediaspringboot.backend.model.Post;
import socialmediaspringboot.backend.model.User.User;
import socialmediaspringboot.backend.repository.PostRepository;
import socialmediaspringboot.backend.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService{

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostMapper postMapper;

    @Override
    @Transactional
    public PostResponseDTO createPost(Long userId, PostDTO postDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Post post = postMapper.toPost(postDTO);
        post.setAuthor(user);

        Post savedPost = postRepository.save(post);
        return postMapper.PostResponseDTO(savedPost);
    }
    @Override
    @Transactional
    public PostResponseDTO  updatePost(Long postId, PostDTO postDTO) {
        Post postExisting = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
       postMapper.updatePostFromDTO(postDTO, postExisting);
       Post updatedPost = postRepository.save(postExisting);
        return postMapper.PostResponseDTO(updatedPost);
    }

    @Override
    @Transactional
    public void deletePost(Long postId) {
        postRepository.deleteById(postId);
    }

    @Override
    public List<Post> getAllPost() {

        return postRepository.findAll();
    }

    @Override
    public PostResponseDTO  getPostById(Long postId) {
        Post post =  postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        return postMapper.PostResponseDTO(post);
    }

    @Override
    public List<PostResponseDTO> getPostByKeyword(String keyword) {
        return postRepository.findByContentContainingIgnoreCase(keyword)
                .stream()
                .map(postMapper::PostResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PostResponseDTO sharePost(Long originalPostId, Long userId, String caption) {
        Post postOrignal = postRepository.findById(originalPostId)
                .orElseThrow(()-> new RuntimeException("Original post not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new RuntimeException("User not found"));
        postOrignal.setShareCount(postOrignal.getShareCount() + 1);
        postRepository.save(postOrignal);
        Post sharePost = Post.builder()
                .author(user)
                .originalPost(postOrignal)
                .content(caption)
                .privacy(postOrignal.getPrivacy())
                .shareCount(0)
                .build();

        return postMapper.PostResponseDTO(postRepository.save(sharePost));
    }
}
