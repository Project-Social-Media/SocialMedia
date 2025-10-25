package socialmediaspringboot.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Role")
public class Role {
    @Id
    private Integer RoleId;

    @Column(nullable = false, unique = true)
    private String RoleName;

    private String RoleDescription;

}
