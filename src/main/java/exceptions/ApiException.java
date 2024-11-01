package exceptions;

import lombok.Getter;

@Getter
public class ApiException extends RuntimeException {

    private int statusCode = -1;

    public ApiException(String message) {
        super(message);
    }

    public ApiException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApiException(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }

    public ApiException(int statusCode, String message, Throwable cause) {
        super(message, cause);
        this.statusCode = statusCode;
    }

    @Override
    public String toString() {
        return "ApiException{" +
                "statusCode=" + statusCode +
                ", message=" + getMessage() +
                '}';
    }
}
