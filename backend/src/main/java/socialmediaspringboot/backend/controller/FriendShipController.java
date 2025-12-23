package socialmediaspringboot.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import socialmediaspringboot.backend.dto.ApiResponse;
import socialmediaspringboot.backend.dto.FriendShipDTO.FriendShipResponseDTO;
import socialmediaspringboot.backend.service.FriendShipService.FriendShipServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/api/friendships")
public class FriendShipController {
    @Autowired
    private FriendShipServiceImpl friendShipService;

    @GetMapping("/{userId}")
    public ApiResponse<List<FriendShipResponseDTO>> getFriends(
            @PathVariable Long userId) {

        return ApiResponse.<List<FriendShipResponseDTO>>builder()
                .result(friendShipService.findFriends(userId))
                .build();
    }

    @GetMapping("/checkFriend")
    public ApiResponse<FriendShipResponseDTO> getFriendship(
            @RequestParam Long userId,
            @RequestParam Long friendId) {

        return ApiResponse.<FriendShipResponseDTO>builder()
                .result(
                        friendShipService
                                .getFriendship(userId, friendId)
                                .orElse(null)
                )
                .build();
    }
    @DeleteMapping("/{userId}/{friendId}")
    public ApiResponse<Void> unFriend(
            @PathVariable Long userId,
            @PathVariable Long friendId) {

        friendShipService.unfriend(userId, friendId);

        return ApiResponse.<Void>builder()
                .message("Unfriend successfully")
                .build();
    }


}
