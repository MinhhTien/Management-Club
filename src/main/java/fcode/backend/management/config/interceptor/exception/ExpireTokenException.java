package fcode.backend.management.config.interceptor.exception;

import org.springframework.http.HttpStatus;

public class ExpireTokenException extends AuthException{
    public ExpireTokenException() {
        super("Token is expired",HttpStatus.REQUEST_TIMEOUT);
    }
}
