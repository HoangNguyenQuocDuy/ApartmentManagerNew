package hnqd.aparmentmanager.common.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
public class ErrorResponse {
    private HttpStatus statusCode;
    private String errorMessage;
    private Object body;

    public ErrorResponse(HttpStatus statusCode, String errorMessage, Object body) {
        this.statusCode = statusCode;
        this.errorMessage = errorMessage;
        this.body = body;
    }

    // Constructor chỉ có 2 tham số
    public ErrorResponse(HttpStatus statusCode, String errorMessage) {
        this(statusCode, errorMessage, null);  // Gán null cho body
    }

    public int getStatusCodeValue() {
        return statusCode.value();
    }

}
