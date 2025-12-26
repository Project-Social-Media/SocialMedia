package socialmediaspringboot.backend.service.FriendShipService;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import socialmediaspringboot.backend.dto.FriendShipDTO.FriendShipResponseDTO;
import socialmediaspringboot.backend.mapper.FriendShipMapper;
import socialmediaspringboot.backend.model.FriendShip;
import socialmediaspringboot.backend.model.FriendShipType;
import socialmediaspringboot.backend.model.User.User;
import socialmediaspringboot.backend.repository.FriendshipRepository;
import socialmediaspringboot.backend.repository.FriendshipTypeRepository;
import socialmediaspringboot.backend.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class FriendShipServiceImpl implements FriendShipService{

    @Autowired
    private FriendshipRepository friendshipRepository;

    @Autowired
    private FriendshipTypeRepository friendshipTypeRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FriendShipMapper friendShipMapper;

    @Override
    public FriendShipResponseDTO makeFriends(Long userId, Long friendId) {
        if (userId.equals(friendId)){
            throw new IllegalArgumentException("Cannot make friend with yourself");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        User friend = userRepository.findById(friendId)
                .orElseThrow(() -> new RuntimeException("Friend not found"));
        FriendShipType friendShipType = friendshipTypeRepository
                .findByFriendshipTypeName("Friend")
                .orElseThrow(() -> new RuntimeException("Friend Type not found"));

        FriendShip f1 = friendshipRepository
                .findByUser_UserIdAndFriend_UserId(userId, friendId)
                .orElseGet(() -> new FriendShip());

        f1.setUser(user);
        f1.setFriend(friend);
        f1.setFriendshipType(friendShipType);

        FriendShip f2 = friendshipRepository
                .findByUser_UserIdAndFriend_UserId(friendId, userId)
                .orElseGet(() -> new FriendShip());

        f2.setUser(friend);
        f2.setFriend(user);
        f2.setFriendshipType(friendShipType);

        friendshipRepository.save(f2);
        FriendShip saved = friendshipRepository.save(f1);

        return friendShipMapper.toResponseDTO(saved);
    }

    @Override
    public void unfriend(Long userId, Long friendId) {
        FriendShipType notFriendType = friendshipTypeRepository
                .findByFriendshipTypeName("Not_Friend")
                .orElseThrow(() -> new RuntimeException("Not friend type not found"));
        friendshipRepository.findByUser_UserIdAndFriend_UserId(userId, friendId)
                .ifPresent(f -> f.setFriendshipType(notFriendType));
        friendshipRepository.findByUser_UserIdAndFriend_UserId(friendId, userId)
                .ifPresent(f -> f.setFriendshipType(notFriendType));

    }

    @Override
    public List<FriendShipResponseDTO> findFriends(Long userId) {
        List<FriendShip> friends =  friendshipRepository.findByUser_UserIdAndFriendshipType_FriendshipTypeName(userId, "Friend");
        return friendShipMapper.toResponseList(friends);
    }

    @Override
    public Optional<FriendShipResponseDTO> getFriendship(Long userId, Long friendId) {
        return friendshipRepository
                .findByUser_UserIdAndFriend_UserId(userId, friendId)
                .map(friendShipMapper::toResponseDTO);
    }
}
