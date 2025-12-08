package socialmediaspringboot.backend.service.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import socialmediaspringboot.backend.dto.Media.MediaRequestDTO;
import socialmediaspringboot.backend.dto.Post.PostDTO;
import socialmediaspringboot.backend.dto.User.ChangePicturePostRequest;
import socialmediaspringboot.backend.dto.User.UpdateProfileRequest;
import socialmediaspringboot.backend.dto.User.UserDTO;
import socialmediaspringboot.backend.dto.User.UserResponseDTO;
import socialmediaspringboot.backend.exception.AppException;
import socialmediaspringboot.backend.exception.ErrorCode;
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

//    @Override
//    public UserResponseDTO changeProfilePicture(MultipartFile file) {
////        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
////        String email = auth.getName();
////
////        User user = userRepository.findByEmail(email)
////                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
////        Privacy privacy = privacyRepository.findByPrivacyName("Public")
////                .orElseThrow(() -> new RuntimeException("Privacy not found"));
////
////        // 1. Tạo post
////        Post post = new Post();
////        post.setAuthor(user);
////        post.setContent(user.getFirstname() + user.getLastname() + " updated profile picture.");
////        post.setPrivacy(privacy);
////        post.setCreatedAt(LocalDateTime.now());
////        post = postRepository.save(post);
////
////        // 2. Chuẩn bị MediaRequestDTO
////        MediaRequestDTO request = new MediaRequestDTO();
////        request.setMediatypeId(mediaTypeRepository.findById(1L).orElseThrow());
////        request.setUploadorder(1);
////        request.setUserId(user);
////        request.setPostId(post);
////
////        // 3. Upload media
////        Media uploadedMedia = mediaService.upload(file, request);
////
////        // 4. Gắn media vào post
////        post.setMediaList(List.of(uploadedMedia));
////        postRepository.save(post);
////
////        // 5. Gắn media vào user (avatar)
////        user.setProfilePicture(uploadedMedia);
////        userRepository.save(user);
////
////        return postMapper.toPostResponseDTO(post);
//    }

//    @Override
//    public UserResponseDTO changeBackgroundPicture(Long mediaId , ChangePicturePostRequest request) {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String email = auth.getName();
//
//        User user = userRepository.findByEmail(email)
//                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
//
//        Media background = mediaRepository.findById(mediaId)
//                .orElseThrow(() -> new RuntimeException("Background picture not found"));
//
//        user.setBackgroundPicture(background);
//
//        userRepository.save(user);
//
//        Long privacyId = request.getPrivacyId() != null ? request.getPrivacyId() : 1L;
//
//        Privacy privacy = privacyRepository.findById(privacyId)
//                .orElseThrow(() -> new RuntimeException("Privacy not found"));
//
//        PostDTO dto = new PostDTO();
//        dto.setContent( user.getFirstname() + " đã cập nhật ảnh bìa.");
//        dto.setPrivacy(privacy);
//
//        postService.createPost(user.getUserId(), dto, null, null);
//
//        return userMapper.toUserResponseDTO(user);
//    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
