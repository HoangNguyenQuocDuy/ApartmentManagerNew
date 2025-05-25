package hnqd.aparmentmanager.common.Enum;

import hnqd.aparmentmanager.common.exceptions.CommonException;
import lombok.Getter;

@Getter
public enum EEmailType {
    WELCOME(0, "welcome", "welcome-email-template.html"),
    PAYMENT_SUCCESS(1, "payment-success", "payment-email-template.html"),
    FIRE_ALERT(2, "fire-alert", "fire-alert-email-template.html"),
    RESET_PASSWORD(3, "reset-password", "reset-password-email-template.html");

    private int code;
    private String name;
    private String templateName;

    EEmailType(int code, String name, String templateName) {
        this.code = code;
        this.name = name;
        this.templateName = templateName;
    }

    public static EEmailType safeValueOfCode(int code) {
        for (EEmailType emailType : EEmailType.values()) {
            if (emailType.getCode() == code) {
                return emailType;
            }
        }

        throw new CommonException.UnknownValuesException("Unknown enum with values: " + code);
    }

    public static EEmailType safeValueOfName(String name) {
        for (EEmailType emailType : EEmailType.values()) {
            if (emailType.getName().equalsIgnoreCase(name)) {
                return emailType;
            }
        }

        throw new CommonException.UnknownValuesException("Unknown enum with values: " + name);
    }
}
