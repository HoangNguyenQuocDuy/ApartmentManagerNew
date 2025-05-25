package hnqd.aparmentmanager.common.Enum;

import hnqd.aparmentmanager.common.exceptions.CommonException;
import lombok.Getter;

@Getter
public enum ECardStatus {
    Pending(0, "Pending"),
    Active(1, "Active"),
    Cancel(1, "Cancel");

    private Integer code;
    private String name;

    ECardStatus(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public static ECardStatus safeValueOfCode(Integer code) {
        for (ECardStatus cardStatus : ECardStatus.values()) {
            if (cardStatus.getCode() == code) {
                return cardStatus;
            }
        }

        throw new CommonException.UnknownValuesException("Unknown enum with values: " + code);
    }

    public static ECardStatus safeValueOfName(String name) {
        for (ECardStatus cardStatus : ECardStatus.values()) {
            if (cardStatus.getName().equalsIgnoreCase(name)) {
                return cardStatus;
            }
        }

        throw new CommonException.UnknownValuesException("Unknown enum with values: " + name);
    }
}
