package hnqd.aparmentmanager.documentservice.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import hnqd.aparmentmanager.common.Enum.EContractStatus;
import hnqd.aparmentmanager.common.Enum.EContractType;
import hnqd.aparmentmanager.common.Enum.ETermFrequency;
import hnqd.aparmentmanager.documentservice.converter.ContractStatusConverter;
import hnqd.aparmentmanager.documentservice.converter.ContractTypeConverter;
import hnqd.aparmentmanager.documentservice.converter.TermFrequencyConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter
@Setter
public class Contract {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String contractNumber;
    @Convert(converter = ContractTypeConverter.class)
    private EContractType contractType;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Integer userId; // FK to user
    private Integer roomId; // FK to room
    private BigDecimal totalAmount;
    @Convert(converter = TermFrequencyConverter.class)
    private ETermFrequency termFrequency; // Contract term (Monthly, Quarterly, ...)
    @Convert(converter = ContractStatusConverter.class)
    private EContractStatus status;

    @JsonIgnore
    @OneToMany(mappedBy = "contract", cascade = CascadeType.ALL)
    private Set<ContractTerm> contractTerms;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
