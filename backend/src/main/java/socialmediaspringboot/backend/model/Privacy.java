package socialmediaspringboot.backend.model;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "privacy")
public class Privacy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "privacyId", nullable = false)
    private Long privacyId;

    @Column(nullable = false, unique = true, length = 50)
    private String privacyName;

    @Column(length = 100)
    private String privacyDescription;
}
