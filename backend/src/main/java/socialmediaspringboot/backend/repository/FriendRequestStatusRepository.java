package socialmediaspringboot.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import socialmediaspringboot.backend.model.FriendRequestStatus;

import java.util.Optional;

public interface FriendRequestStatusRepository extends JpaRepository<FriendRequestStatus, Integer> {
    Optional<FriendRequestStatus> findByStatusNameIgnoreCase(String statusName);
}
