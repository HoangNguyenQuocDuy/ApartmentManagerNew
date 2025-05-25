package hnqd.aparmentmanager.paymentservice.converter;

import hnqd.aparmentmanager.common.Enum.EPaymentMethod;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class PaymentMethodConverter implements AttributeConverter<EPaymentMethod, String> {
    @Override
    public String convertToDatabaseColumn(EPaymentMethod ePaymentMethod) {
        return ePaymentMethod != null ? ePaymentMethod.getName() : null;
    }

    @Override
    public EPaymentMethod convertToEntityAttribute(String dbData) {
        return dbData != null ? EPaymentMethod.safeValueOfName(dbData) : null;
    }
}
