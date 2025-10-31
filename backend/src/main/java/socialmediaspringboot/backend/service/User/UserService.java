package socialmediaspringboot.backend.service.User;

import org.springframework.data.domain.Page;
import socialmediaspringboot.backend.dto.UserDTO;
import socialmediaspringboot.backend.model.User.User;

import java.util.List;

public interface UserService {
    User createUser(UserDTO request);
    UserDTO getUser(long id);
    List<User> getAllUsers();
    UserDTO getMyInfo();
    Page<User>getUsersInPage(int pageNumber, int pageSize);
    UserDTO updateUser(long userId, UserDTO request);
    void deleteUser(long id);
}
