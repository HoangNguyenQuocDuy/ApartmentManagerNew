package hnqd.aparmentmanager.documentservice.converter;

import hnqd.aparmentmanager.common.Enum.EContractStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ContractStatusConverter implements AttributeConverter<EContractStatus, String> {
    @Override
    public String convertToDatabaseColumn(EContractStatus eContractStatus) {
        return eContractStatus != null ? eContractStatus.getName() : null;
    }

    @Override
    public EContractStatus convertToEntityAttribute(String dbData) {
        return dbData != null ? EContractStatus.safeValueOfName(dbData) : null;
    }
}
