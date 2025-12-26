package socialmediaspringboot.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import socialmediaspringboot.backend.model.FriendShipType;

import java.util.Optional;


public interface FriendshipTypeRepository extends JpaRepository<FriendShipType, Integer> {
    Optional<FriendShipType> findByFriendshipTypeName(String name);
}
