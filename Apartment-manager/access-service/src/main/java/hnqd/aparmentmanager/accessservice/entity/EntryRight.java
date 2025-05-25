package hnqd.aparmentmanager.accessservice.entity;

import hnqd.aparmentmanager.accessservice.converter.CardStatusConverter;
import hnqd.aparmentmanager.common.Enum.ECardStatus;
import hnqd.aparmentmanager.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class EntryRight extends BaseEntity {

    @Basic
    @Convert(converter = CardStatusConverter.class)
    private ECardStatus status;
    @ManyToOne
    @JoinColumn(name = "relativeId", referencedColumnName = "id", nullable = false)
    private Relative relative;

}
