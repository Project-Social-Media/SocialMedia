package socialmediaspringboot.backend.model.User;

import jakarta.persistence.*;
import lombok.*;
import org.joda.time.DateTime;
import socialmediaspringboot.backend.model.Gender;
import socialmediaspringboot.backend.model.Role;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "Users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userId", nullable = false)
    private Long userId;

    private String email;
    private String password;
    private String firstname;
    private String lastname;

    @ManyToOne
    @JoinColumn(name = "GenderId")
    private Gender gender;

    private Date birth;
    private DateTime createdAt;

    private Set<Role> roles = new HashSet<>();
}
