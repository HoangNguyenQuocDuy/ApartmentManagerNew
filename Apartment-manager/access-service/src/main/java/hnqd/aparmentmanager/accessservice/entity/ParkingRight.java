package hnqd.aparmentmanager.accessservice.entity;

import hnqd.aparmentmanager.accessservice.converter.CardStatusConverter;
import hnqd.aparmentmanager.common.enums.ECardStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ParkingRight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Basic
    @Column(name = "typeOfVehicle")
    private String typeOfVehicle;
    @Basic
    @Column(name = "licensePlates")
    private String licensePlates;
    @Basic
    @Convert(converter = CardStatusConverter.class)
    private ECardStatus status;
    @ManyToOne
    @JoinColumn(name = "relativeId", referencedColumnName = "id", nullable = false)
    private Relative relative;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

}
