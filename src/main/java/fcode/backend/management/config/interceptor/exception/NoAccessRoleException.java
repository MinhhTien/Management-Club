package fcode.backend.management.config.interceptor.exception;

import org.springframework.http.HttpStatus;

public class NoAccessRoleException extends AuthException{
    public NoAccessRoleException() {
        super("You are not allowed to access this method",HttpStatus.FORBIDDEN);
    }
}
