package socialmediaspringboot.backend.service.FriendShipService;

import socialmediaspringboot.backend.dto.FriendShipDTO.FriendShipResponseDTO;
import socialmediaspringboot.backend.model.FriendShip;

import java.util.List;
import java.util.Optional;

public interface FriendShipService {
    FriendShipResponseDTO makeFriends(Long userId, Long friendId);

    void unfriend(Long userId, Long friendId);

    List<FriendShipResponseDTO > findFriends(Long userId);
    Optional<FriendShipResponseDTO > getFriendship(Long userId, Long friendId);

}
