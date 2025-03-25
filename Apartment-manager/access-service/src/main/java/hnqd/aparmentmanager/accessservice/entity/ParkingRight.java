package hnqd.aparmentmanager.accessservice.entity;

import hnqd.aparmentmanager.accessservice.converter.CardStatusConverter;
import hnqd.aparmentmanager.common.Enum.ECardStatus;
import hnqd.aparmentmanager.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ParkingRight extends BaseEntity {

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

}
