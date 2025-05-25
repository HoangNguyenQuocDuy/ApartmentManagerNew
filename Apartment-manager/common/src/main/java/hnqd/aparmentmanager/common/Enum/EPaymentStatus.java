package hnqd.aparmentmanager.common.Enum;

import hnqd.aparmentmanager.common.exceptions.CommonException;

public enum EPaymentStatus {
    PENDING(0, "Pending"),
    PAID(1, "Paid"),
    FAILED(2, "Failed"),
    CANCELLED(3, "Cancelled"),
    REFUNDED(4, "Refunded"),
    UNDER_REVIEW(5, "Under Review"), // for especial payment method, need to considerate
    EXPIRED(6, "Expired"),
    FAILED_AUTOMATICALLY(7, "Failed Automatically"),
    UNPAID(1, "UnPaid");

    private int code;
    private String name;

    EPaymentStatus(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static EPaymentStatus safeValueOfCode(int code) {
        for (EPaymentStatus termFrequency : EPaymentStatus.values()) {
            if (termFrequency.getCode() == code) {
                return termFrequency;
            }
        }

        throw new CommonException.UnknownValuesException("Unknown enum with values: " + code);
    }

    public static EPaymentStatus safeValueOfName(String name) {
        for (EPaymentStatus termFrequency : EPaymentStatus.values()) {
            if (termFrequency.getName().equalsIgnoreCase(name)) {
                return termFrequency;
            }
        }

        throw new CommonException.UnknownValuesException("Unknown enum with values: " + name);
    }
}
