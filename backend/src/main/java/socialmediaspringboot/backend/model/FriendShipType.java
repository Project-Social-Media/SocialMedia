package socialmediaspringboot.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "friendshipType")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FriendShipType {
    @Id
    @Column(name = "friendshipTypeId")
    private int friendshipTypeId;

    @Column(name = "friendshipTypeName",nullable = false,unique = true)
    private String friendshipTypeName;
}
