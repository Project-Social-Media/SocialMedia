package socialmediaspringboot.backend.service.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContextException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import socialmediaspringboot.backend.constant.PredefinedRoles;
import socialmediaspringboot.backend.dto.UpdateProfileRequest;
import socialmediaspringboot.backend.dto.UserDTO;
import socialmediaspringboot.backend.dto.UserResponseDTO;
import socialmediaspringboot.backend.exception.AppException;
import socialmediaspringboot.backend.exception.ErrorCode;
import socialmediaspringboot.backend.mapper.UserMapper;
import socialmediaspringboot.backend.model.Gender;
import socialmediaspringboot.backend.model.Role;
import socialmediaspringboot.backend.model.User.User;
import socialmediaspringboot.backend.repository.GenderRepository;
import socialmediaspringboot.backend.repository.RoleRepository;
import socialmediaspringboot.backend.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

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

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
