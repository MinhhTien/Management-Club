package fcode.backend.management.config.interceptor.exception;

import org.springframework.http.HttpStatus;

public class NotFoundApiException extends AuthException{
    public NotFoundApiException() {
        super("Api is not exist",HttpStatus.NOT_FOUND);
    }
}
