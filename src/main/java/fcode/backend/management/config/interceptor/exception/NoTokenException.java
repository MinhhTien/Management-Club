package fcode.backend.management.config.interceptor.exception;

import org.springframework.http.HttpStatus;

public class NoTokenException extends AuthException{
    public NoTokenException() {
        super("User must have token to access",HttpStatus.UNAUTHORIZED);
    }
}
