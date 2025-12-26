package socialmediaspringboot.backend.service.FriendRequest;

import socialmediaspringboot.backend.dto.FriendRequestDTO.FriendRequestCreateRequestDTO;
import socialmediaspringboot.backend.dto.FriendRequestDTO.FriendRequestResponseDTO;
import socialmediaspringboot.backend.model.FriendRequest;

import java.util.List;

public interface FriendRequestService {

    FriendRequestResponseDTO sendFriendRequest(Long senderId, FriendRequestCreateRequestDTO requestDTO);

    FriendRequestResponseDTO acceptFriendRequest(Long requestId, Long receiverId);

    void rejectFriendRequest(Long currentUserId, Long targetUserId);
    void removeFriendRequest(Long requestId);
    List<FriendRequestResponseDTO> getAllFriendRequest();

    List<FriendRequestResponseDTO> getPendingRequests(Long receiverId);

}
