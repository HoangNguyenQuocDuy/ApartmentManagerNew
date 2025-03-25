package hnqd.aparmentmanager.common.Enum;

import hnqd.aparmentmanager.common.exceptions.CommonException;
import lombok.Getter;

@Getter
public enum ERelativeType {
    FATHER(0, "Father"),
    MOTHER(1, "Mother"),
    HUSBAND(2, "Husband"),
    WIFE(3, "Wife"),
    SON(4, "Son"),
    DAUGHTER(5, "Daughter"),
    BROTHER(6, "Brother"),
    SISTER(7, "Sister"),
    GRANDFATHER(8, "Grandfather"),
    GRANDMOTHER(9, "Grandmother"),
    UNCLE(10, "Uncle"),
    AUNT(11, "Aunt"),
    FRIEND(12, "Friend"),
    OTHER(13, "Other");

    private Integer code;
    private String name;

    ERelativeType(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public static ERelativeType safeValueOfCode(Integer code) {
        for (ERelativeType relative : ERelativeType.values()) {
            if (relative.getCode() == code) {
                return relative;
            }
        }

        throw new CommonException.UnknownValuesException("Unknown enum with values: " + code);
    }

    public static ERelativeType safeValueOfName(String name) {
        for (ERelativeType relative : ERelativeType.values()) {
            if (relative.getName().equalsIgnoreCase(name)) {
                return relative;
            }
        }

        throw new CommonException.UnknownValuesException("Unknown enum with values: " + name);
    }
}
