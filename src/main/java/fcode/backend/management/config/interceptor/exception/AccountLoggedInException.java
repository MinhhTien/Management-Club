package fcode.backend.management.config.interceptor.exception;

import org.springframework.http.HttpStatus;

public class AccountLoggedInException extends AuthException{
    public AccountLoggedInException() {
        super("Account is login in another device", HttpStatus.UNAUTHORIZED);
    }
}