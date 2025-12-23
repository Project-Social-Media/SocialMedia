package socialmediaspringboot.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import socialmediaspringboot.backend.model.FriendRequest;

import java.util.List;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {

    boolean existsBySender_UserIdAndReceiver_UserId(Long senderId, Long receiverId);

    boolean existsBySender_UserIdAndReceiver_UserIdAndStatus_StatusName(
            Long senderId, Long receiverId, String statusName
    );

    List<FriendRequest> findByReceiver_UserIdAndStatus_StatusName(Long receiverId, String statusName);
}
