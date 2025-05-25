package hnqd.aparmentmanager.accessservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import hnqd.aparmentmanager.accessservice.converter.RelativeTypeConverter;
import hnqd.aparmentmanager.common.Enum.ERelativeType;
import hnqd.aparmentmanager.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@Entity
@Getter
@Setter
public class Relative extends BaseEntity {

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

}
