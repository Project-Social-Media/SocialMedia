package socialmediaspringboot.backend.service.Post;


import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import socialmediaspringboot.backend.dto.Media.MediaRequestDTO;
import socialmediaspringboot.backend.dto.Media.MediaResponseDTO;
import socialmediaspringboot.backend.dto.Post.PostDTO;
import socialmediaspringboot.backend.dto.Post.PostResponseDTO;
import socialmediaspringboot.backend.mapper.MediaMapper;
import socialmediaspringboot.backend.mapper.PostMapper;
import socialmediaspringboot.backend.model.Media;
import socialmediaspringboot.backend.model.MediaType;
import socialmediaspringboot.backend.model.Post;
import socialmediaspringboot.backend.model.User.User;
import socialmediaspringboot.backend.repository.MediaRepository;
import socialmediaspringboot.backend.repository.MediaTypeRepository;
import socialmediaspringboot.backend.repository.PostRepository;
import socialmediaspringboot.backend.repository.UserRepository;


import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Page;
import socialmediaspringboot.backend.service.Media.MediaServiceImpl;

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
    @Autowired
    private MediaMapper mediaMapper;

    @Autowired
    private MediaRepository mediaRepository;
    @Autowired
    private MediaTypeRepository mediaTypeRepository;
    @Autowired
    private MediaServiceImpl mediaService;


    @Override
    @Transactional
    public PostResponseDTO createPost(Long userId, PostDTO postDTO, MultipartFile[] files, MediaRequestDTO mediaRequestDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Post post = postMapper.toPost(postDTO);
        post.setAuthor(user);

        Post savedPost = postRepository.save(post);
        if (files != null) {
            int order = 0;

            for (MultipartFile file : files) {

                // 1. Upload file -> lấy DTO chứa secure_url, cloud_id
                MediaResponseDTO uploaded = mediaService.upload(file, mediaRequestDTO);

                // 2. Tạo Media entity từ MediaRequestDTO
                MediaRequestDTO req = new MediaRequestDTO();
                req.setUploadorder(order++);
                // các trường khác trong mediaRequestDTO nếu bạn có

                Media media = mediaMapper.toMedia(req);

                // 3. Set thêm dữ liệu upload trả về
                media.setMediaUrl(uploaded.getMediaUrl());
                media.setCloudId(uploaded.getCloudId());

                // 4. Set user & post
                media.setUserId(user);
                media.setPostId(savedPost);

                // 5. Set media type nếu cần (từ upload)
                if (uploaded.getMediatypeId() != null) {
                    MediaType type = mediaTypeRepository.findById(uploaded.getMediatypeId())
                            .orElseThrow(() -> new RuntimeException("Media type not found"));
                    media.setMediatypeId(type);
                }

                // 6. Save entity
                mediaRepository.save(media);
            }
        }
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
