package socialmediaspringboot.backend.service.Post;


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


import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Page;

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
        return postMapper.toPostResponseDTO(savedPost);
    }
    @Override
    @Transactional
    public PostResponseDTO  updatePost(Long postId, PostDTO postDTO) {
        Post postExisting = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
       postMapper.updatePostFromDTO(postDTO, postExisting);
        postExisting.setUpdatedAt(LocalDateTime.now());

        Post updatedPost = postRepository.save(postExisting);

        return postMapper.toPostResponseDTO(updatedPost);
    }

    @Override
    @Transactional
    public void deletePost(Long postId) {
        postRepository.deleteById(postId);
    }

    @Override
    @Transactional
    public Page<PostResponseDTO> getPostsByUser(Long userId, Pageable pageable) {
        Pageable sortedPageable = Pageable.unpaged();
        if (pageable != null) {
            sortedPageable = PageRequest.of(
                    pageable.getPageNumber(),
                    pageable.getPageSize(),
                    Sort.by("createdAt").descending()
            );
        }

        Page<Post> postPage = postRepository.findByAuthor_UserId(userId, sortedPageable);

        return postPage.map(post -> postMapper.toPostResponseDTO(post));
    }

    @Override
    public List<Post> getAllPost(Long userId) {

        return postRepository.findAllByAuthor_UserIdOrderByCreatedAtDesc(userId);
    }

    @Override
    public PostResponseDTO  getPostById(Long postId) {
        Post post =  postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        return postMapper.toPostResponseDTO(post);
    }

    @Override
    public List<PostResponseDTO> getPostByKeyword(String keyword) {
        return postRepository.findByContentContainingIgnoreCase(keyword)
                .stream()
                .map(postMapper::toPostResponseDTO)
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

        return postMapper.toPostResponseDTO(postRepository.save(sharePost));
    }
}
