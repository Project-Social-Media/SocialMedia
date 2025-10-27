package socialmediaspringboot.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import socialmediaspringboot.backend.model.Gender;

import java.util.Optional;

public interface GenderRepository extends JpaRepository<Gender, Integer> {
    Optional<Gender> findByGenderName(String genderName);
}