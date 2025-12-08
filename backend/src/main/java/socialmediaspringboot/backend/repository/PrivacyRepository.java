package socialmediaspringboot.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import socialmediaspringboot.backend.model.Privacy;

import java.util.Optional;

public interface PrivacyRepository extends JpaRepository<Privacy, Long> {
    Optional<Privacy> findByPrivacyName(String privacyName);
}