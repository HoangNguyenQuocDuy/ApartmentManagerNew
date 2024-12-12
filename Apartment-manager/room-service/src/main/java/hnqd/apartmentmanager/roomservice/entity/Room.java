package hnqd.apartmentmanager.roomservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
public class Room {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;
    @Basic
    @Column(name = "name")
    private String name;
    @Basic
    @Column(name = "status")
    private String status;
    @Basic
    @Column(name = "image")
    private String image;
    @ManyToOne
    @JoinColumn(name = "roomTypeId", referencedColumnName = "id", nullable = false)
    private RoomType roomType;
    //    @JsonIgnore
//    @OneToMany(mappedBy = "room")
//    private Collection<Invoice> invoices;
//    @JsonIgnore
//    @OneToMany(mappedBy = "room")
//    private Collection<User> users;
//    @JsonIgnore
//    @OneToMany(mappedBy = "room")
//    private Collection<VisitLog> visitLogs;
}
