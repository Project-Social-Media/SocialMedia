package socialmediaspringboot.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "Role")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role {
    @Id
    private Integer roleId;

    @Column(nullable = false, unique = true)
    private String roleName;

    private String roleDescription;
}
