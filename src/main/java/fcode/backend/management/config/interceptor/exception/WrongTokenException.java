package fcode.backend.management.config.interceptor.exception;

import org.springframework.http.HttpStatus;

public class WrongTokenException extends AuthException{
    public WrongTokenException() {
        super("Token is not valid",HttpStatus.UNAUTHORIZED);
    }
}
