package hnqd.aparmentmanager.lockerservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;

@Setter
@Getter
@Entity
public class Locker {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;
    @Basic
    @Column(name = "status")
    private String status;
    @JsonIgnore
    @OneToMany(mappedBy = "locker")
    private Collection<Order> orders;
    @JsonIgnore
    @OneToMany(mappedBy = "locker")
    private Collection<User> users;

}
