package socialmediaspringboot.backend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import socialmediaspringboot.backend.dto.FriendRequestDTO.FriendRequestResponseDTO;
import socialmediaspringboot.backend.model.FriendRequest;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FriendRequestMapper {

    @Mapping(source = "friendRequestId", target = "friendRequestId")
    @Mapping(source = "sender.userId", target = "senderId")
    @Mapping(expression = "java(friendRequest.getSender().getFirstname() + \" \" + friendRequest.getSender().getLastname())",
    target = "senderName")
    @Mapping(source = "sender.profilePicture.mediaUrl", target = "senderAvatar")
    @Mapping(source = "receiver.userId", target = "receiverId")
    @Mapping(source = "status.statusName", target = "status")
    @Mapping(source = "createdAt", target = "createdAt")
    FriendRequestResponseDTO toFriendRequestResponseDTO(FriendRequest friendRequest);

    List<FriendRequestResponseDTO> toFriendRequestResponseList(List<FriendRequest> entities);
}
