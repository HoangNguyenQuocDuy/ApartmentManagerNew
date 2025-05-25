package hnqd.aparmentmanager.documentservice.converter;

import hnqd.aparmentmanager.common.Enum.EContractType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ContractTypeConverter implements AttributeConverter<EContractType, String> {
    @Override
    public String convertToDatabaseColumn(EContractType eContractType) {
        System.out.println("Converting to DB: " + (eContractType != null ? eContractType.getName() : "null"));
        return eContractType != null ? eContractType.getName() : null;
    }

    @Override
    public EContractType convertToEntityAttribute(String dbData) {
        return dbData != null ? EContractType.safeValueOfName(dbData) : null;
    }
}
