package hnqd.aparmentmanager.documentservice.converter;

import hnqd.aparmentmanager.common.Enum.EPaymentStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class PaymentStatusConverter implements AttributeConverter<EPaymentStatus, String> {
    @Override
    public String convertToDatabaseColumn(EPaymentStatus ePaymentStatus) {
        return ePaymentStatus != null ? ePaymentStatus.getName() : null;
    }

    @Override
    public EPaymentStatus convertToEntityAttribute(String dbData) {
        return dbData != null ? EPaymentStatus.safeValueOfName(dbData) : null;
    }
}
