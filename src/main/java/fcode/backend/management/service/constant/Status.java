package fcode.backend.management.service.constant;

import lombok.Getter;

@Getter
public enum Status {
    AVAILABLE_STATUS("available"),
    UNAVAILABLE_STATUS("unavailable");
    private final String message;
    Status(String message) {
        this.message = message;
    }
}
