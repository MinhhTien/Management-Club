package fcode.backend.management.service.constant;

import lombok.Getter;

@Getter
public enum ServiceStatusCode {

    OK_STATUS(200),
    NOT_FOUND_STATUS(404),
    BAD_REQUEST_STATUS(400);

    private final int code;
    ServiceStatusCode(int code) {
        this.code = code;
    }
}
