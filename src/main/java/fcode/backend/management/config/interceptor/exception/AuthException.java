package fcode.backend.management.config.interceptor.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class AuthException extends RuntimeException{
    private HttpStatus httpStatus;

    public AuthException(String message,HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
        super(message);
    }
}
