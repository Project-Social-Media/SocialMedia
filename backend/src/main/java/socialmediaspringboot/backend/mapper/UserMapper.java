package socialmediaspringboot.backend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import socialmediaspringboot.backend.dto.UserDTO;
import socialmediaspringboot.backend.dto.UserResponseDTO;
import socialmediaspringboot.backend.model.User.User;

import java.util.Optional;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target="roles", ignore = true)
    @Mapping(target = "userId", ignore = true)
    User toUser(UserDTO request);

    @Mapping(target="roles", ignore = true)
    UserResponseDTO toUserResponseDTO (User user);

    @Mapping(target="roles", ignore = true)
    @Mapping(target = "userId", ignore = true)
    void updateUser(@MappingTarget User user, UserDTO request);
}