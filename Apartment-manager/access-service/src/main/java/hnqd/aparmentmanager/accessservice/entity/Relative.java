package hnqd.aparmentmanager.accessservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import hnqd.aparmentmanager.accessservice.converter.RelativeTypeConverter;
import hnqd.aparmentmanager.common.enums.ERelativeType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter
@Setter
public class Relative {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "full_name", nullable = false)
    private String fullName;
    @Column(name = "avatar")
    private String avatar;
    @Column(name = "idCard")
    private String idCard;
    @Column(name = "relationship", nullable = false)
    @Convert(converter = RelativeTypeConverter.class)
    private ERelativeType relationship;
    @Column(name = "contractId", nullable = false)
    private Integer contractId;
    @JsonIgnore
    @OneToMany(mappedBy = "relative")
    private Set<EntryRight> entryRights;
    @JsonIgnore
    @OneToMany(mappedBy = "relative")
    private Set<ParkingRight> parkingRights;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

}
