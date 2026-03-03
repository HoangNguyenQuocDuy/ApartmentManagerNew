package hnqd.aparmentmanager.accessservice.entity;

import hnqd.aparmentmanager.accessservice.converter.CardStatusConverter;
import hnqd.aparmentmanager.common.enums.ECardStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class EntryRight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
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
