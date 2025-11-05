package socialmediaspringboot.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "gender")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Gender {
    @Id
    private Integer genderId;

    @Column(nullable = false,unique = true)
    private String genderName;

    private String genderDescription;
}
