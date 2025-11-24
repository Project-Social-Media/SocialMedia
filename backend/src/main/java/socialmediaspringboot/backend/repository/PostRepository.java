package socialmediaspringboot.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import socialmediaspringboot.backend.model.Post;
import socialmediaspringboot.backend.model.Privacy;
import socialmediaspringboot.backend.model.User.User;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findByContentContainingIgnoreCase(String keyword);
    List<Post> findAllByAuthorInAndPrivacyInOrderByCreatedAtDesc(
            List<User> authors,
            List<Privacy> privacies
    );
    List<Post> findByAuthor_UserId(Long userId);
}
