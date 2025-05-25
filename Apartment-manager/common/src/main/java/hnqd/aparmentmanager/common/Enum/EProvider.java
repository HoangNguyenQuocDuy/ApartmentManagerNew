package hnqd.aparmentmanager.common.Enum;

import hnqd.aparmentmanager.common.exceptions.CommonException;
import lombok.Getter;

@Getter
public enum EProvider {
    VNPAY(0, "VNPay"),
    MOMO(1, "Momo");

    private Integer code;
    private String name;

    EProvider(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public static hnqd.aparmentmanager.common.Enum.EProvider safeValueOfCode(Integer code) {
        for (hnqd.aparmentmanager.common.Enum.EProvider termFrequency : hnqd.aparmentmanager.common.Enum.EProvider.values()) {
            if (termFrequency.getCode() == code) {
                return termFrequency;
            }
        }

        throw new CommonException.UnknownValuesException("Unknown enum with values: " + code);
    }

    public static hnqd.aparmentmanager.common.Enum.EProvider safeValueOfName(String name) {
        for (hnqd.aparmentmanager.common.Enum.EProvider termFrequency : hnqd.aparmentmanager.common.Enum.EProvider.values()) {
            if (termFrequency.getName().equalsIgnoreCase(name)) {
                return termFrequency;
            }
        }

        throw new CommonException.UnknownValuesException("Unknown enum with values: " + name);
    }
}
