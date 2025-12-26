package socialmediaspringboot.backend.service.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import socialmediaspringboot.backend.dto.Media.MediaRequestDTO;
import socialmediaspringboot.backend.dto.Media.MediaResponseDTO;
import socialmediaspringboot.backend.dto.Post.PostDTO;
import socialmediaspringboot.backend.dto.Post.PostResponseDTO;
import socialmediaspringboot.backend.dto.User.ChangePicturePostRequest;
import socialmediaspringboot.backend.dto.User.UpdateProfileRequest;
import socialmediaspringboot.backend.dto.User.UserDTO;
import socialmediaspringboot.backend.dto.User.UserResponseDTO;
import socialmediaspringboot.backend.exception.AppException;
import socialmediaspringboot.backend.exception.ErrorCode;
import socialmediaspringboot.backend.mapper.MediaMapper;
import socialmediaspringboot.backend.mapper.UserMapper;
import socialmediaspringboot.backend.model.*;
import socialmediaspringboot.backend.model.User.User;
import socialmediaspringboot.backend.repository.*;
import socialmediaspringboot.backend.service.Media.MediaServiceImpl;
import socialmediaspringboot.backend.service.Post.PostServiceImpl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.List;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private GenderRepository genderRepository;

    @Autowired
    private MediaRepository mediaRepository;

    @Autowired
    private PrivacyRepository privacyRepository;
    @Autowired
    private PostServiceImpl postService;

    @Autowired
    private MediaServiceImpl mediaService;

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private MediaTypeRepository mediaTypeRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MediaMapper mediaMapper;

    @Override
    public User createUser(UserDTO request){
        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        Role userRole = roleRepository.findByRoleName("USER")
                .orElseThrow(() -> new RuntimeException("Role not found"));
        HashSet<Role> roles = new HashSet<>();
        roles.add(userRole);
        user.setRoles(roles);
//        user.getRoles().add(userRole);

        LocalDateTime now = LocalDateTime.now(ZoneId.of("UTC"));
        user.setCreatedAt(now);

        try{
           return user = userRepository.save(user);
        }catch(DataIntegrityViolationException e){
            throw new AppException(ErrorCode.USERNAME_EXISTED);
        }
    }

    @Override
    public UserResponseDTO getUser(Long id){
        return userMapper.toUserResponseDTO(userRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_FOUND))
        );
    }

    @Override
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    @Override
    public UserResponseDTO  getMyInfo(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new RuntimeException("User not found"));

        return userMapper.toUserResponseDTO(user);
    }

    @Override
    public Page<User> getUsersInPage(int pageNumber, int pageSize) {
        PageRequest firstPageRequest = PageRequest.of(pageNumber, pageSize);
        return userRepository.findAll(firstPageRequest);
    }

    @Override
    public UserResponseDTO updateUser(UpdateProfileRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        user.setFirstname(request.getFirstname());
        user.setLastname(request.getLastname());
        user.setBirth(request.getBirth());

        if (request.getGenderId() != null){
            Gender gender = genderRepository.findById(request.getGenderId())
                    .orElseThrow(() -> new RuntimeException("Gender not found"));
            user.setGender(gender);
        }
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        userMapper.updateUser(user, request);
        User updatedUser = userRepository.save(user);
        return userMapper.toUserResponseDTO(updatedUser);
    }

    @Override
    @Transactional
    public UserResponseDTO changeProfilePicture(
            MultipartFile file,
            ChangePicturePostRequest request
    ) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        // upload avatar
        MediaRequestDTO mediaReq = new MediaRequestDTO();
        mediaReq.setUploadorder(0);
        mediaReq.setPostId(null); // Upload avatar → chưa gắn post
        mediaReq.setUserId(user.getUserId());

        MediaResponseDTO uploaded = mediaService.upload(file, mediaReq);

        // 2. --- CONVERT DTO → ENTITY ---
        Media avatar = mediaMapper.toMedia(mediaReq);
        avatar.setMediaUrl(uploaded.getMediaUrl());
        avatar.setCloudId(uploaded.getCloudId());
        avatar.setUserId(user);

        // Gắn MediaType (từ upload logic đã xử lý ID)
        if (uploaded.getMediatypeId() != null) {
            MediaType type = mediaTypeRepository.findById(uploaded.getMediatypeId())
                    .orElseThrow(() -> new RuntimeException("Media type not found"));
            avatar.setMediatypeId(type);
        }

        avatar = mediaRepository.save(avatar);

        // 3. --- GẮN VÀO USER ---
        user.setProfilePicture(avatar);
        userRepository.save(user);

        // 4. --- LẤY PRIVACY ---
        Long privacyId = request.getPrivacyId() != null ? request.getPrivacyId() : 1L;
        Privacy privacy = privacyRepository.findById(privacyId)
                .orElseThrow(() -> new RuntimeException("Privacy not found"));

        // 5. --- TẠO POST ---
        PostDTO postDTO = new PostDTO();
        postDTO.setContent(
                request.getContent() != null
                        ? request.getContent()
                        : user.getFirstname() + " đã cập nhật ảnh đại diện."
        );
        postDTO.setPrivacy(privacy);

        // Gọi createPost
        PostResponseDTO created = postService.createPost(user.getUserId(), postDTO, null );

        // Sau tạo post → update media vào post
        avatar.setPost(postRepository.findById(created.getPostId()).orElseThrow());
        mediaRepository.save(avatar);

        return userMapper.toUserResponseDTO(user);
    }


    @Override
    @Transactional
    public UserResponseDTO changeBackgroundPicture(
            MultipartFile file,
            ChangePicturePostRequest request
    ) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        // Upload media
        MediaRequestDTO mediaReq = new MediaRequestDTO();
        mediaReq.setUploadorder(0);
        mediaReq.setPostId(null);
        mediaReq.setUserId(user.getUserId());

        MediaResponseDTO uploaded = mediaService.upload(file, mediaReq);

        Media background = mediaMapper.toMedia(mediaReq);
        background.setMediaUrl(uploaded.getMediaUrl());
        background.setCloudId(uploaded.getCloudId());
        background.setUserId(user);

        if (uploaded.getMediatypeId() != null) {
            MediaType type = mediaTypeRepository.findById(uploaded.getMediatypeId())
                    .orElseThrow(() -> new RuntimeException("Media type not found"));
            background.setMediatypeId(type);
        }

        background = mediaRepository.save(background);

        //Update user
        user.setBackgroundPicture(background);
        userRepository.save(user);

        // Privacy
        Long privacyId = request.getPrivacyId() != null ? request.getPrivacyId() : 1L;
        Privacy privacy = privacyRepository.findById(privacyId)
                .orElseThrow(() -> new RuntimeException("Privacy not found"));

        // Create post
        PostDTO postDTO = new PostDTO();
        postDTO.setContent(
                request.getContent() != null
                        ? request.getContent()
                        : user.getFirstname() + " đã cập nhật ảnh bìa."
        );
        postDTO.setPrivacy(privacy);

        PostResponseDTO created = postService.createPost(user.getUserId(), postDTO, null);

        // Gắn media vào post
        background.setPost(postRepository.findById(created.getPostId()).orElseThrow());
        mediaRepository.save(background);

        return userMapper.toUserResponseDTO(user);
    }


    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
