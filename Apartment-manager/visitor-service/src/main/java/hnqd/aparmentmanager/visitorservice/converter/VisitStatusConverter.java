package hnqd.aparmentmanager.visitorservice.converter;

import hnqd.aparmentmanager.common.Enum.EVisitStatus;
import jakarta.persistence.AttributeConverter;

public class VisitStatusConverter implements AttributeConverter<EVisitStatus, String> {

    @Override
    public String convertToDatabaseColumn(EVisitStatus eVisitStatus) {
        return eVisitStatus != null ? eVisitStatus.getName() : null;
    }

    @Override
    public EVisitStatus convertToEntityAttribute(String dbData) {
        return dbData != null ? EVisitStatus.safeValueOfName(dbData) : null;
    }
}
