package hnqd.aparmentmanager.accessservice.converter;

import hnqd.aparmentmanager.common.Enum.ERelativeType;
import jakarta.persistence.AttributeConverter;

public class RelativeTypeConverter implements AttributeConverter<ERelativeType, String> {
    @Override
    public String convertToDatabaseColumn(ERelativeType eRelativeType) {
        return eRelativeType != null ? eRelativeType.getName() : null;
    }

    @Override
    public ERelativeType convertToEntityAttribute(String dbData) {
        return dbData != null ? ERelativeType.safeValueOfName(dbData) : null;
    }
}
