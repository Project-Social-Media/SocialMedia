package socialmediaspringboot.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import socialmediaspringboot.backend.model.Media;

public interface MediaRepository extends JpaRepository<Media, Long> {

}
