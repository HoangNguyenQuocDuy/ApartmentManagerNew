package hnqd.aparmentmanager.common.Enum;

import hnqd.aparmentmanager.common.exceptions.CommonException;

public enum EChatType {

    MESSAGE(0, "@text"),
    IMAGE(0, "@image");

    private int code;
    private String name;

    EChatType(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static EChatType safeValueOfCode(int code) {
        for (EChatType termFrequency : EChatType.values()) {
            if (termFrequency.getCode() == code) {
                return termFrequency;
            }
        }

        throw new CommonException.UnknownValuesException("Unknown enum with values: " + code);
    }

    public static EChatType safeValueOfName(String name) {
        for (EChatType termFrequency : EChatType.values()) {
            if (termFrequency.getName().equalsIgnoreCase(name)) {
                return termFrequency;
            }
        }

        throw new CommonException.UnknownValuesException("Unknown enum with values: " + name);
    }
}
