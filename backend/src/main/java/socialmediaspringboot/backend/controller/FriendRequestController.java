package socialmediaspringboot.backend.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import socialmediaspringboot.backend.dto.ApiResponse;
import socialmediaspringboot.backend.dto.FriendRequestDTO.FriendRequestCreateRequestDTO;
import socialmediaspringboot.backend.dto.FriendRequestDTO.FriendRequestResponseDTO;
import socialmediaspringboot.backend.service.FriendRequest.FriendRequestService;
import socialmediaspringboot.backend.service.FriendRequest.FriendRequestServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/api/friends")
public class FriendRequestController {
    @Autowired
    FriendRequestServiceImpl friendRequestService;

    @PostMapping("/request/{userId}")
    public ApiResponse<FriendRequestResponseDTO> sendFriendRequest (@PathVariable("userId") Long senderId,
                                                                    @RequestBody @Valid FriendRequestCreateRequestDTO friendRequestCreateRequestDTO){
        FriendRequestResponseDTO sendRequest = friendRequestService.sendFriendRequest(senderId, friendRequestCreateRequestDTO);
        return ApiResponse.<FriendRequestResponseDTO>builder()
                .result(sendRequest)
                .message("Send friend request successfully")
                .build();
    }

    @PutMapping("/accept/{requestId}")
    public ApiResponse<FriendRequestResponseDTO> acceptFriendRequest(@PathVariable Long requestId,
                                                                     @RequestParam Long receiverId){
        FriendRequestResponseDTO acceptRequest = friendRequestService.acceptFriendRequest(requestId,receiverId);
        return ApiResponse.<FriendRequestResponseDTO>builder()
                .result(acceptRequest)
                .message("Accept friend request successfully")
                .build();
    }
    @PutMapping("/reject/{targetUserId}")
    public ApiResponse<Void> rejectFriendRequest(
            @RequestParam Long currentUserId,
            @PathVariable Long targetUserId
    ){
        friendRequestService.rejectFriendRequest(currentUserId,targetUserId);
        return ApiResponse.<Void>builder()
                .message("Reject request successfully")
                .build();
    }
    @GetMapping("/requests")
    public ApiResponse<List<FriendRequestResponseDTO>> getPendingRequests(@RequestParam Long receiverId){
        List<FriendRequestResponseDTO> requests = friendRequestService.getPendingRequests(receiverId);
        return ApiResponse.<List<FriendRequestResponseDTO>>builder()
                .result(requests)
                .message("Get pending friend requests successfully")
                .build();
    }
    @DeleteMapping("/{requestId}")
    public ApiResponse<Void> removeFriendRequest(@PathVariable Long requestId) {
        friendRequestService.removeFriendRequest(requestId);
        return ApiResponse.<Void>builder()
                .message("Remove friend request successfully")
                .build();
    }
    @GetMapping
    public ApiResponse<List<FriendRequestResponseDTO>> getAllFriendRequests() {
        List<FriendRequestResponseDTO> requests = friendRequestService.getAllFriendRequest();
        return ApiResponse.<List<FriendRequestResponseDTO>>builder()
                .result(requests)
                .message("Get all friend requests successfully")
                .build();
    }
}
