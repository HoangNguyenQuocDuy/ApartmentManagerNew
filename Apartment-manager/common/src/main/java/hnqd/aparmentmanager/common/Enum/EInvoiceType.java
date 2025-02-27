package hnqd.aparmentmanager.common.Enum;

import hnqd.aparmentmanager.common.exceptions.CommonException;

public enum EInvoiceType {
    RENT(0, "Rent"),
    UTILITY(1, "Utility"),
    MAINTENANCE(2, "Maintain"),
    ELECTRIC(3, "Electric"),
    WATER(4, "Water");

    private int code;
    private String name;

    EInvoiceType(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static EInvoiceType safeValueOfCode(int code) {
        for (EInvoiceType termFrequency : EInvoiceType.values()) {
            if (termFrequency.getCode() == code) {
                return termFrequency;
            }
        }

        throw new CommonException.UnknownValuesException("Unknown enum with values: " + code);
    }

    public static EInvoiceType safeValueOfName(String name) {
        for (EInvoiceType termFrequency : EInvoiceType.values()) {
            if (termFrequency.getName().equalsIgnoreCase(name)) {
                return termFrequency;
            }
        }

        throw new CommonException.UnknownValuesException("Unknown enum with values: " + name);
    }
}
