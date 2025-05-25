package hnqd.aparmentmanager.common.Enum;

import hnqd.aparmentmanager.common.exceptions.CommonException;

public enum EContractType {
    RENTAL(0, "Rental"),
    BUY(1, "Buy"),
    OTHER(2, "Other");

    private int code;
    private String name;

    EContractType(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static EContractType safeValueOfCode(int code) {
        for (EContractType contractType : EContractType.values()) {
            if (contractType.getCode() == code) {
                return contractType;
            }
        }

        throw new CommonException.UnknownValuesException("Unknown enum with values: " + code);
    }

    public static EContractType safeValueOfName(String name) {
        for (EContractType contractType : EContractType.values()) {
            if (contractType.getName().equalsIgnoreCase(name)) {
                return contractType;
            }
        }

        throw new CommonException.UnknownValuesException("Unknown enum with values: " + name);
    }
}
