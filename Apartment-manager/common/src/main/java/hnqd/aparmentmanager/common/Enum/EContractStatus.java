package hnqd.aparmentmanager.common.Enum;

import hnqd.aparmentmanager.common.exceptions.CommonException;

public enum EContractStatus {
    PENDING(0, "Pending"),
    ACTIVE(1, "Active"),
    TERMINATED(2, "Terminated");

    private int code;
    private String name;

    EContractStatus(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static EContractStatus safeValueOfCode(int code) {
        for (EContractStatus termFrequency : EContractStatus.values()) {
            if (termFrequency.getCode() == code) {
                return termFrequency;
            }
        }

        throw new CommonException.UnknownValuesException("Unknown enum with values: " + code);
    }

    public static EContractStatus safeValueOfName(String name) {
        for (EContractStatus termFrequency : EContractStatus.values()) {
            if (termFrequency.getName().equalsIgnoreCase(name)) {
                return termFrequency;
            }
        }

        throw new CommonException.UnknownValuesException("Unknown enum with values: " + name);
    }
}
