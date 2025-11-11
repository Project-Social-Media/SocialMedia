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
import socialmediaspringboot.backend.mapper.UserMapper;
import socialmediaspringboot.backend.model.Role;
import socialmediaspringboot.backend.model.User.User;
import socialmediaspringboot.backend.repository.RoleRepository;
import socialmediaspringboot.backend.repository.UserRepository;

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
//        HashSet<Role> roles = new HashSet<>();
//        roles.add(userRole);
//        user.setRoles(roles);
        user.getRoles().add(userRole);

        try{
           return user = userRepository.save(user);
        }catch(DataIntegrityViolationException e){
            throw new ApplicationContextException("errror", e);// placeholder before implement global error handler
        }
    }

    @Override
    public UserDTO getUser(long id){
        return userMapper.toUserDTO(userRepository.findById(id)
                .orElseThrow(() -> new ApplicationContextException("error")));
        //needs to implement exception handler from global handler after
    }

    @Override
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    @Override
    public UserDTO getMyInfo(){
        var context = SecurityContextHolder.getContext();
        String email = context.getAuthentication().getName();

        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new ApplicationContextException("error") // placeholder before implement global error handler
        );

        return userMapper.toUserDTO(user);
    }

    @Override
    public Page<User> getUsersInPage(int pageNumber, int pageSize) {
        PageRequest firstPageRequest = PageRequest.of(pageNumber, pageSize);
        return userRepository.findAll(firstPageRequest);
    }

    @Override
    public UserDTO updateUser(long userId, UserDTO request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApplicationContextException("error")); //need to throw new exception handler in global handler after
        userMapper.updateUser(user, request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        //need to get all the roles of the account here
        //var roles = roleRepository.find(request.getRoles());
        //user.setRoles(new HashSet<>(roles));

        return userMapper.toUserDTO(userRepository.save(user));
    }

    @Override
    public void deleteUser(long id) {
        userRepository.deleteById(id);
    }
}
