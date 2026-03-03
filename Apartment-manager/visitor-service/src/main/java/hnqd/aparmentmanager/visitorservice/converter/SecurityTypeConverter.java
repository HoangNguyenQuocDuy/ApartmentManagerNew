package hnqd.aparmentmanager.visitorservice.converter;

import hnqd.aparmentmanager.common.enums.ESecurityType;
import jakarta.persistence.AttributeConverter;

public class SecurityTypeConverter implements AttributeConverter<ESecurityType, String> {

    @Override
    public String convertToDatabaseColumn(ESecurityType eSecurityType) {
        return eSecurityType != null ? eSecurityType.getName() : null;
    }

    @Override
    public ESecurityType convertToEntityAttribute(String dbData) {
        return dbData != null ? ESecurityType.safeValueOfName(dbData) : null;
    }
}
