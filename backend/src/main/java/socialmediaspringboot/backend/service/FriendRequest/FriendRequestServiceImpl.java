package socialmediaspringboot.backend.service.FriendRequest;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import socialmediaspringboot.backend.dto.FriendRequestDTO.FriendRequestCreateRequestDTO;
import socialmediaspringboot.backend.dto.FriendRequestDTO.FriendRequestResponseDTO;
import socialmediaspringboot.backend.mapper.FriendRequestMapper;
import socialmediaspringboot.backend.model.FriendRequest;
import socialmediaspringboot.backend.model.FriendRequestStatus;
import socialmediaspringboot.backend.model.User.User;
import socialmediaspringboot.backend.repository.FriendRequestRepository;
import socialmediaspringboot.backend.repository.FriendRequestStatusRepository;
import socialmediaspringboot.backend.repository.UserRepository;
import socialmediaspringboot.backend.service.FriendShipService.FriendShipServiceImpl;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class FriendRequestServiceImpl implements FriendRequestService{

    @Autowired
    private FriendRequestRepository friendRequestRepository;

    @Autowired
    private FriendRequestStatusRepository friendRequestStatusRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FriendRequestMapper friendRequestMapper;

    @Autowired
    private FriendShipServiceImpl friendShipService;
    @Override
    public FriendRequestResponseDTO sendFriendRequest(Long senderId, FriendRequestCreateRequestDTO requestDTO) {
        if (senderId.equals(requestDTO.getReceiverId())){
            throw new IllegalArgumentException("Cannot send friend request to yourself");
        }
        User sender = userRepository.findById(senderId)
             .orElseThrow(() -> new RuntimeException("Sender not found"));
        User receiver = userRepository.findById(requestDTO.getReceiverId())
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        if (friendRequestRepository.existsBySender_UserIdAndReceiver_UserId(senderId, receiver.getUserId())){
            throw new IllegalStateException("Friend request already exists");
        }
        FriendRequestStatus pendingStatus = friendRequestStatusRepository
                .findByStatusNameIgnoreCase("Pending")
                .orElseThrow(() -> new RuntimeException("Pending status not found"));
        FriendRequest friendRequest = new FriendRequest();
        friendRequest.setSender(sender);
        friendRequest.setReceiver(receiver);
        friendRequest.setStatus(pendingStatus);
        friendRequest.setCreatedAt(LocalDateTime.now());
        return friendRequestMapper.toFriendRequestResponseDTO(friendRequestRepository.save(friendRequest));
    }

    @Override
    public FriendRequestResponseDTO acceptFriendRequest(Long requestId, Long receiverId) {
        FriendRequest friendRequest = friendRequestRepository.findById(requestId)
                .orElseThrow(()-> new RuntimeException("Friend request not found"));
        if (!friendRequest.getReceiver().getUserId().equals(receiverId)) {
            throw new IllegalArgumentException("You are not allowed to accept this request");
        }
        FriendRequestStatus acceptedStatus = friendRequestStatusRepository
                .findByStatusNameIgnoreCase("Accepted")
                .orElseThrow(() -> new RuntimeException("Accepted status not found"));
        friendRequest.setStatus(acceptedStatus);
        friendRequestRepository.save(friendRequest);
        friendShipService.makeFriends(friendRequest.getSender().getUserId(), friendRequest.getReceiver().getUserId());
        return friendRequestMapper.toFriendRequestResponseDTO(friendRequest);
    }

    @Override
    public void rejectFriendRequest(Long currentUserId, Long targetUserId) {
        FriendRequest friendRequest = friendRequestRepository
                .findAll()
                .stream()
                .filter( fr -> (
                        fr.getSender().getUserId().equals(currentUserId)
                        && fr.getReceiver().getUserId().equals(targetUserId)
                        || (fr.getSender().getUserId().equals(targetUserId)
                                && fr.getReceiver().getUserId().equals(currentUserId))
                        )
                )
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Friend relationship not found"));

        FriendRequestStatus rejectedStatus = friendRequestStatusRepository
                .findByStatusNameIgnoreCase("Rejected")
                .orElseThrow(() -> new RuntimeException("Rejected status not found"));
        friendRequest.setStatus(rejectedStatus);
        friendRequestRepository.save(friendRequest);
    }

    @Override
    public void removeFriendRequest(Long requestId) {
         friendRequestRepository.deleteById(requestId);;
    }

    @Override
    public List<FriendRequestResponseDTO> getAllFriendRequest() {
        List<FriendRequest> requests = friendRequestRepository.findAll();
        return friendRequestMapper.toFriendRequestResponseList(requests);
    }

    @Override
    public List<FriendRequestResponseDTO> getPendingRequests(Long receiverId) {
        List<FriendRequest> requests = friendRequestRepository.findByReceiver_UserIdAndStatus_StatusName(receiverId, "Pending");
        return friendRequestMapper.toFriendRequestResponseList(requests);
    }
}
