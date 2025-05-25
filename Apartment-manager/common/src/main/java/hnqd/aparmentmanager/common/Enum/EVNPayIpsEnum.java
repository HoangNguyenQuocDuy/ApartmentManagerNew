package hnqd.aparmentmanager.common.Enum;

import hnqd.aparmentmanager.common.exceptions.CommonException;
import lombok.Getter;

@Getter
public enum EVNPayIpsEnum {
    SUCCESS("00", "Payment Success");

    private String code;
    private String name;

    EVNPayIpsEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static EVNPayIpsEnum safeValueOfCode(String code) {
        for (EVNPayIpsEnum termFrequency : EVNPayIpsEnum.values()) {
            if (termFrequency.getCode() == code) {
                return termFrequency;
            }
        }

        throw new CommonException.UnknownValuesException("Unknown enum with values: " + code);
    }

    public static EVNPayIpsEnum safeValueOfName(String name) {
        for (EVNPayIpsEnum termFrequency : EVNPayIpsEnum.values()) {
            if (termFrequency.getName().equalsIgnoreCase(name)) {
                return termFrequency;
            }
        }

        throw new CommonException.UnknownValuesException("Unknown enum with values: " + name);
    }
}
