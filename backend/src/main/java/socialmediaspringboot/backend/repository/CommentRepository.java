package socialmediaspringboot.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import socialmediaspringboot.backend.model.Comment;
import socialmediaspringboot.backend.model.Post;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findALlByPost_PostId(Long postId);

    List<Comment> findALlCommentByAuthor_UserId(Long userId);
}
