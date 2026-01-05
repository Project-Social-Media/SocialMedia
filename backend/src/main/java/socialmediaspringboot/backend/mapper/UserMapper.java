package socialmediaspringboot.backend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import socialmediaspringboot.backend.dto.User.UpdateProfileRequest;
import socialmediaspringboot.backend.dto.User.UserDTO;
import socialmediaspringboot.backend.dto.User.UserResponseDTO;
import socialmediaspringboot.backend.model.User.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target="roles", ignore = true)
    @Mapping(target = "userId", ignore = true)
    User toUser(UserDTO request);

    @Mapping(source="roles", target = "roles")
    UserResponseDTO toUserResponseDTO (User user);
    @Mapping(target="roles", ignore = true)
    @Mapping(target = "userId", ignore = true)
    void updateUser(@MappingTarget User user, UpdateProfileRequest request);
}