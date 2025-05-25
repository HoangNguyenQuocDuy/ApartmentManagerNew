package hnqd.aparmentmanager.accessservice.converter;

import hnqd.aparmentmanager.common.Enum.ECardStatus;
import jakarta.persistence.AttributeConverter;

public class CardStatusConverter implements AttributeConverter<ECardStatus, String> {
    @Override
    public String convertToDatabaseColumn(ECardStatus eCardStatus) {
        return eCardStatus != null ? eCardStatus.getName() : null;
    }

    @Override
    public ECardStatus convertToEntityAttribute(String dbData) {
        return dbData != null ? ECardStatus.safeValueOfName(dbData) : null;
    }
}
