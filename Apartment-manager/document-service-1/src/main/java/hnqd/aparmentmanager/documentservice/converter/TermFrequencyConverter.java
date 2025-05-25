package hnqd.aparmentmanager.documentservice.converter;

import hnqd.aparmentmanager.common.Enum.ETermFrequency;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class TermFrequencyConverter implements AttributeConverter<ETermFrequency, String> {

    @Override
    public String convertToDatabaseColumn(ETermFrequency eTermFrequency) {
        return eTermFrequency != null ? eTermFrequency.getName() : null;
    }

    @Override
    public ETermFrequency convertToEntityAttribute(String dbData) {
        return dbData != null ? ETermFrequency.safeValueOfName(dbData) : null;
    }
}
