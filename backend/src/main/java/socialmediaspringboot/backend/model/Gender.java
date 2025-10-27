package socialmediaspringboot.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "Gender")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Gender {
    @Id
    private Integer GenderId;

    @Column(nullable = false,unique = true)
    private String genderName;

    private String GenderDescription;
}
