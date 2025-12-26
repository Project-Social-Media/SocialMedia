package socialmediaspringboot.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "friendRequestStatus")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FriendRequestStatus {

    @Id
    @Column(name = "statusId")
    private Integer statusId;

    @Column(name = "statusName", nullable = false)
    private String statusName;

    @Column(name = "statusDescription")
    private String statusDescription;

    @OneToMany(mappedBy = "status")
    private List<FriendRequest> friendRequestList;
}
