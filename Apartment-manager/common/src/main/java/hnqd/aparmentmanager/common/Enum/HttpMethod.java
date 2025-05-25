package hnqd.aparmentmanager.common.Enum;

import hnqd.aparmentmanager.common.exceptions.CommonException;

public enum HttpMethod {
    GET(0, "GET"),
    POST(1, "POST"),
    PUT(2, "PUT"),
    PATCH(3, "PATCH"),
    DELETE(4, "DELETE")
    ;

    HttpMethod(int code, String name) {
        this.code = code;
        this.name = name;
    }

    private int code;
    private String name;

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static HttpMethod safeValueOfName(String name) {
        for (HttpMethod httpMethod : HttpMethod.values()) {
            if (httpMethod.getName().equals(name)) {
                return httpMethod;
            }
        }
        throw new CommonException.UnknownValuesException("Unknown enum with values: " + name);
    }
}
