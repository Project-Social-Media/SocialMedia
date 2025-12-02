package socialmediaspringboot.backend.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import socialmediaspringboot.backend.model.Post;
import socialmediaspringboot.backend.model.Privacy;
import socialmediaspringboot.backend.model.User.User;


import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findByContentContainingIgnoreCase(String keyword);

    List<Post> findAllByAuthor_UserIdOrderByCreatedAtDesc(Long userId);

    Page<Post> findByAuthor_UserId(Long userId, Pageable pageable);
}
