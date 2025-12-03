package socialmediaspringboot.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "mediaType")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MediaType {
    @Id
    private Integer mediaTypeId;

    @Column(nullable = false,unique = true)
    private String typeName;

    private String typeDescription;
}
