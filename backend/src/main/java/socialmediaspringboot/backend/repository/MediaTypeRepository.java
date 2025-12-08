package socialmediaspringboot.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import socialmediaspringboot.backend.model.Media;
import socialmediaspringboot.backend.model.MediaType;

public interface MediaTypeRepository extends JpaRepository<MediaType, Integer> {
}
