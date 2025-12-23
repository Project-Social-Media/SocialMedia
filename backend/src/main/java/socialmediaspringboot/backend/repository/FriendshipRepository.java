package socialmediaspringboot.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import socialmediaspringboot.backend.model.FriendShip;

import java.util.List;
import java.util.Optional;

public interface FriendshipRepository extends JpaRepository<FriendShip, Long> {
    Optional<FriendShip> findByUser_UserIdAndFriend_UserId(Long userId, Long friendId);
    List<FriendShip> findByUser_UserIdAndFriendshipType_FriendshipTypeName(
            Long userId,
            String friendshipTypeName
    );
}
