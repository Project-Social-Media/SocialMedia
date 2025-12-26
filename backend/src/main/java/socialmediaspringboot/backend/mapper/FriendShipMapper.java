package socialmediaspringboot.backend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import socialmediaspringboot.backend.dto.FriendShipDTO.FriendShipResponseDTO;
import socialmediaspringboot.backend.model.FriendShip;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FriendShipMapper {
    @Mapping(source = "friendshipId", target = "friendshipId")
    @Mapping(source = "user.userId", target = "userId")
    @Mapping(source = "friend.userId", target = "friendId")
    @Mapping(
            expression = "java(friendShip.getFriend().getFirstname() + \" \" + friendShip.getFriend().getLastname())",
            target = "friendName"
    )
    @Mapping(source = "friendshipType.friendshipTypeName", target = "friendshipType")
    @Mapping(source = "createdAt", target = "createdAt")
    FriendShipResponseDTO toResponseDTO(FriendShip friendShip);

    List<FriendShipResponseDTO> toResponseList(List<FriendShip> entities);
}
