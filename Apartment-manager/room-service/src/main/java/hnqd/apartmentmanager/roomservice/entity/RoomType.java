package hnqd.apartmentmanager.roomservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Collection;

@Entity
@Getter
@Setter
public class RoomType {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;
    @Basic
    @Column(name = "type")
    private String type;
    @Basic
    @Column
    private BigDecimal rentPrice;
    @Basic
    @Column
    private BigDecimal roomPrice;
    @JsonIgnore
    @OneToMany(mappedBy = "roomType")
    private Collection<Room> rooms;
}
