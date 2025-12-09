package socialmediaspringboot.backend.service.User;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;
import socialmediaspringboot.backend.dto.User.ChangePicturePostRequest;
import socialmediaspringboot.backend.dto.User.UpdateProfileRequest;
import socialmediaspringboot.backend.dto.User.UserDTO;
import socialmediaspringboot.backend.dto.User.UserResponseDTO;
import socialmediaspringboot.backend.model.User.User;

import java.util.List;

public interface UserService {
    User createUser(UserDTO request);
    UserResponseDTO getUser(Long id);
    List<User> getAllUsers();
    UserResponseDTO getMyInfo();
    Page<User>getUsersInPage(int pageNumber, int pageSize);
    UserResponseDTO updateUser(UpdateProfileRequest request);
    UserResponseDTO changeProfilePicture(MultipartFile file, ChangePicturePostRequest request);
    UserResponseDTO changeBackgroundPicture(MultipartFile file, ChangePicturePostRequest request);

    void deleteUser(Long id);
}
