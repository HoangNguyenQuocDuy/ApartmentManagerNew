package hnqd.aparmentmanager.common.Enum;

import hnqd.aparmentmanager.common.exceptions.CommonException;

public enum EPaymentMethod {
    CASH(0, "Cash"),
    TRANSFER(1, "Transfer"),
    EWALLET(2, "EWallet");

    private int code;
    private String name;

    EPaymentMethod(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static EPaymentMethod safeValueOfCode(int code) {
        for (EPaymentMethod termFrequency : EPaymentMethod.values()) {
            if (termFrequency.getCode() == code) {
                return termFrequency;
            }
        }

        throw new CommonException.UnknownValuesException("Unknown enum with values: " + code);
    }

    public static EPaymentMethod safeValueOfName(String name) {
        for (EPaymentMethod termFrequency : EPaymentMethod.values()) {
            if (termFrequency.getName().equalsIgnoreCase(name)) {
                return termFrequency;
            }
        }

        throw new CommonException.UnknownValuesException("Unknown enum with values: " + name);
    }
}
