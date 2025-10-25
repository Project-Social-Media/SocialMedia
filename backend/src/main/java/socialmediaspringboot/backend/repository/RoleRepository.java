package socialmediaspringboot.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import socialmediaspringboot.backend.model.Role;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(String RoleName);
}
