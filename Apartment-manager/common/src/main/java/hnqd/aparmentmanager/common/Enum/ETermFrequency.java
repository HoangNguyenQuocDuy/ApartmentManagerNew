package hnqd.aparmentmanager.common.Enum;

import hnqd.aparmentmanager.common.exceptions.CommonException;

public enum ETermFrequency {
    MONTHLY(0, "Monthly"),
    QUARTERLY(1, "Quaterly"),
    YEARLY(2, "Yearly");

    private int code;
    private String name;

    ETermFrequency(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static ETermFrequency safeValueOfCode(int code) {
        for (ETermFrequency termFrequency : ETermFrequency.values()) {
            if (termFrequency.getCode() == code) {
                return termFrequency;
            }
        }

        throw new CommonException.UnknownValuesException("Unknown enum with values: " + code);
    }

    public static ETermFrequency safeValueOfName(String name) {
        for (ETermFrequency termFrequency : ETermFrequency.values()) {
            if (termFrequency.getName().equalsIgnoreCase(name)) {
                return termFrequency;
            }
        }

        throw new CommonException.UnknownValuesException("Unknown enum with values: " + name);
    }
}
