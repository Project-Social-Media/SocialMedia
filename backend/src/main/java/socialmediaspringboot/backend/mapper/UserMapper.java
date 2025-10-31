package socialmediaspringboot.backend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import socialmediaspringboot.backend.dto.UserDTO;
import socialmediaspringboot.backend.model.User.User;

import java.util.Optional;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target="roles", ignore = true)
    User toUser(UserDTO request);

    @Mapping(target="roles", ignore = true)
    UserDTO toUserDTO (User user);

    @Mapping(target="roles", ignore = true)
    void updateUser(@MappingTarget User user, UserDTO request);
}