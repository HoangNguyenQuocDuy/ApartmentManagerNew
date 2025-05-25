package hnqd.aparmentmanager.documentservice.converter;

import hnqd.aparmentmanager.common.Enum.EInvoiceType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class InvoiceTypeConverter implements AttributeConverter<EInvoiceType, String> {
    @Override
    public String convertToDatabaseColumn(EInvoiceType eInvoiceType) {
        return eInvoiceType != null ? eInvoiceType.getName() : null;
    }

    @Override
    public EInvoiceType convertToEntityAttribute(String dbData) {
        return dbData != null ? EInvoiceType.safeValueOfName(dbData) : null;
    }
}
