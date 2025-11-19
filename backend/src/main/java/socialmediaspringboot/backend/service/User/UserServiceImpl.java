package socialmediaspringboot.backend.service.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContextException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import socialmediaspringboot.backend.constant.PredefinedRoles;
import socialmediaspringboot.backend.dto.UserDTO;
import socialmediaspringboot.backend.dto.UserResponseDTO;
import socialmediaspringboot.backend.exception.AppException;
import socialmediaspringboot.backend.exception.ErrorCode;
import socialmediaspringboot.backend.mapper.UserMapper;
import socialmediaspringboot.backend.model.Role;
import socialmediaspringboot.backend.model.User.User;
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
    private PasswordEncoder passwordEncoder;

    @Override
    public User createUser(UserDTO request){
        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        Role userRole = roleRepository.findByRoleName("ROLE_USER")
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
    public UserResponseDTO getUser(long id){
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
        var context = SecurityContextHolder.getContext();
        String email = context.getAuthentication().getName();

        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_FOUND)
        );

        return userMapper.toUserResponseDTO(user);
    }

    @Override
    public Page<User> getUsersInPage(int pageNumber, int pageSize) {
        PageRequest firstPageRequest = PageRequest.of(pageNumber, pageSize);
        return userRepository.findAll(firstPageRequest);
    }

    @Override
    public UserResponseDTO updateUser(long userId, UserDTO request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND)); //need to throw new exception handler in global handler after
        userMapper.updateUser(user, request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        //need to get all the roles of the account here
        //var roles = roleRepository.find(request.getRoles());
        //user.setRoles(new HashSet<>(roles));

        return userMapper.toUserResponseDTO(userRepository.save(user));
    }

    @Override
    public void deleteUser(long id) {
        userRepository.deleteById(id);
    }
}
