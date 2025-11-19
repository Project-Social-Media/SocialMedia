package socialmediaspringboot.backend.service.User;

import org.springframework.data.domain.Page;
import socialmediaspringboot.backend.dto.UserDTO;
import socialmediaspringboot.backend.dto.UserResponseDTO;
import socialmediaspringboot.backend.model.User.User;

import java.util.List;

public interface UserService {
    User createUser(UserDTO request);
    UserResponseDTO getUser(long id);
    List<User> getAllUsers();
    UserResponseDTO getMyInfo();
    Page<User>getUsersInPage(int pageNumber, int pageSize);
    UserResponseDTO updateUser(long userId, UserDTO request);
    void deleteUser(long id);
}
